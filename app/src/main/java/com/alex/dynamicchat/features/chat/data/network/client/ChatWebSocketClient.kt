package com.alex.dynamicchat.features.chat.data.network.client

import com.alex.dynamicchat.features.chat.domain.model.ConnectionEvent
import com.alex.dynamicchat.features.chat.domain.model.ConnectionState
import com.alex.dynamicchat.features.chat.domain.model.WebSocketError
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ChatWebSocketClient(
    private val okHttpClient: OkHttpClient
) : WebSocketListener() {

    @Volatile
    private var webSocket: WebSocket? = null

    private val _incomingMessages = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val incomingMessages: SharedFlow<String> = _incomingMessages

    private val _connectionEvents = MutableSharedFlow<ConnectionEvent>(
        replay = 1,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionEvents: SharedFlow<ConnectionEvent> = _connectionEvents

    @Volatile
    private var connectionState: ConnectionState = ConnectionState.DISCONNECTED

    fun send(message: String) {
        val ws = webSocket ?: run {
            _connectionEvents.tryEmit(ConnectionEvent.Error(WebSocketError.NullSocket))
            return
        }

        if (connectionState != ConnectionState.CONNECTED) {
            _connectionEvents.tryEmit(ConnectionEvent.Error(WebSocketError.NotReady))
            return
        }

        if (!ws.send(message)) {
            _connectionEvents.tryEmit(ConnectionEvent.Error(WebSocketError.SendFailed))
        }
    }

    fun connect(url: String) {
        if (connectionState != ConnectionState.DISCONNECTED) return

        connectionState = ConnectionState.CONNECTING
        _connectionEvents.tryEmit(ConnectionEvent.Connecting)

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            webSocket = okHttpClient.newWebSocket(request, this)
        } catch (e: Exception) {
            connectionState = ConnectionState.DISCONNECTED
            _connectionEvents.tryEmit(ConnectionEvent.Error(WebSocketError.Unknown(e)))
        }
    }

    fun disconnect() {
        val ws = webSocket
        connectionState = ConnectionState.DISCONNECTED
        ws?.close(1000, "Client closing")
        webSocket = null
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        this.webSocket = webSocket
        connectionState = ConnectionState.CONNECTED
        _connectionEvents.tryEmit(ConnectionEvent.Connected)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        _incomingMessages.tryEmit(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        _incomingMessages.tryEmit(bytes.utf8())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        connectionState = ConnectionState.DISCONNECTED
        _connectionEvents.tryEmit(ConnectionEvent.Closed(reason))
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        connectionState = ConnectionState.DISCONNECTED
        _connectionEvents.tryEmit(ConnectionEvent.Closed(reason))
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        connectionState = ConnectionState.DISCONNECTED
        this.webSocket = null
        _connectionEvents.tryEmit(ConnectionEvent.Error(WebSocketError.Unknown(t)))
    }
}
