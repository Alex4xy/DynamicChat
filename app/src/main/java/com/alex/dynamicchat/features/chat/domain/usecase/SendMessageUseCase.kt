package com.alex.dynamicchat.features.chat.domain.usecase

import com.alex.dynamicchat.core.app.StateResource
import com.alex.dynamicchat.features.chat.domain.model.Message
import com.alex.dynamicchat.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(message: Message): StateResource<Unit> {
        return try {
            repository.sendMessage(message)
            StateResource.Success(Unit)
        } catch (e: Exception) {
            StateResource.Error(e.message.orEmpty(), e)
        }
    }
}