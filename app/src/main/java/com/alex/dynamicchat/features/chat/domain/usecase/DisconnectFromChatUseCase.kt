package com.alex.dynamicchat.features.chat.domain.usecase

import com.alex.dynamicchat.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class DisconnectFromChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() {
        repository.disconnect()
    }
}