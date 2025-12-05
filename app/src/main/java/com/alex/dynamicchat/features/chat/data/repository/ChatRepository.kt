package com.alex.dynamicchat.features.chat.data.repository

import com.alex.dynamicchat.features.chat.data.network.client.ChatWebSocketClient
import com.alex.dynamicchat.features.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val incomingMessages: Flow<Message>
    val connectionEvents: Flow<ChatWebSocketClient.ConnectionEvent>

    fun connect()
    fun disconnect()
    suspend fun sendMessage(message: Message)
}