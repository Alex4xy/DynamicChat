package com.alex.dynamicchat.features.chat.domain.usecase

import com.alex.dynamicchat.features.chat.data.network.client.ChatWebSocketClient
import com.alex.dynamicchat.features.chat.data.repository.ChatRepository
import com.alex.dynamicchat.features.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<Message> = repository.incomingMessages
}

class ObserveConnectionEventsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<ChatWebSocketClient.ConnectionEvent> =
        repository.connectionEvents
}

class ConnectToChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() {
        repository.connect()
    }
}

class DisconnectFromChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() {
        repository.disconnect()
    }
}

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(message: Message) {
        repository.sendMessage(message)
    }
}
