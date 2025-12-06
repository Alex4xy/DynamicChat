package com.alex.dynamicchat.features.chat.data.repository

import com.alex.dynamicchat.features.chat.data.network.client.ChatWebSocketClient
import com.alex.dynamicchat.features.chat.domain.model.ConnectionEvent
import com.alex.dynamicchat.features.chat.data.network.dto.MessageDto
import com.alex.dynamicchat.features.chat.data.network.dto.toDomain
import com.alex.dynamicchat.features.chat.domain.model.Message
import com.alex.dynamicchat.features.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val webSocketClient: ChatWebSocketClient
) : ChatRepository {

    private val currentUserId = "me"

    override val incomingMessages: Flow<Message> =
        webSocketClient.incomingMessages.map { text ->
            MessageDto(
                id = System.currentTimeMillis(),
                senderId = "echo-bot",
                senderName = "EchoBot",
                text = text,
                timestamp = System.currentTimeMillis(),
                isUnread = true
            ).toDomain(currentUserId = currentUserId)
        }

    override val connectionEvents: Flow<ConnectionEvent> =
        webSocketClient.connectionEvents

    override fun connect() {
        webSocketClient.connect("wss://ws.postman-echo.com/raw")
    }

    override fun disconnect() {
        webSocketClient.disconnect()
    }

    override suspend fun sendMessage(message: Message) {
        webSocketClient.send(message.text)
    }
}
