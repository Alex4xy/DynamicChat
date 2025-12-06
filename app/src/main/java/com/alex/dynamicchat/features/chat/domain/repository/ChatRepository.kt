package com.alex.dynamicchat.features.chat.domain.repository

import com.alex.dynamicchat.features.chat.domain.model.ConnectionEvent
import com.alex.dynamicchat.features.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val incomingMessages: Flow<Message>
    val connectionEvents: Flow<ConnectionEvent>

    fun connect()
    fun disconnect()
    suspend fun sendMessage(message: Message)
}