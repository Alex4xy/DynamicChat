package com.alex.dynamicchat.features.chat.data.network.client

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    val incomingMessages: SharedFlow<String> = _incomingMessages.asSharedFlow()

    private val _connectionEvents = MutableSharedFlow<ConnectionEvent>(
        replay = 1,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionEvents: SharedFlow<ConnectionEvent> = _connectionEvents.asSharedFlow()

    @Volatile
    private var connectionState = ConnectionState.DISCONNECTED

    sealed class ConnectionEvent {
        object Connecting : ConnectionEvent()
        object Connected : ConnectionEvent()
        data class Error(val message: String) : ConnectionEvent()
        data class Closed(val reason: String) : ConnectionEvent()
    }

    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
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
            _connectionEvents.tryEmit(ConnectionEvent.Error(e.message ?: "Connection failed"))
        }
    }

    fun send(message: String) {
        val ws = webSocket ?: run {
            _connectionEvents.tryEmit(ConnectionEvent.Error("Not connected"))
            return
        }

        if (connectionState != ConnectionState.CONNECTED) {
            _connectionEvents.tryEmit(ConnectionEvent.Error("Connection not ready"))
            return
        }

        if (!ws.send(message)) {
            _connectionEvents.tryEmit(ConnectionEvent.Error("Failed to send"))
        }
    }

    fun disconnect() {
        connectionState = ConnectionState.DISCONNECTED
        webSocket?.close(1000, "Client closing")
        webSocket = null
    }

    fun isConnected(): Boolean =
        connectionState == ConnectionState.CONNECTED

    override fun onOpen(webSocket: WebSocket, response: Response) {
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
        _connectionEvents.tryEmit(ConnectionEvent.Error(t.message ?: "Unknown error"))
        this.webSocket = null
    }
}