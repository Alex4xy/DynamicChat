package com.alex.dynamicchat.features.chat.domain.usecase

import com.alex.dynamicchat.core.app.StateResource
import com.alex.dynamicchat.features.chat.domain.model.Message
import com.alex.dynamicchat.features.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<StateResource<Message>> {
        return repository.incomingMessages
            .map<Message, StateResource<Message>> { msg ->
                StateResource.Success(msg)
            }
            .catch { e ->
                emit(StateResource.Error(e.message.orEmpty(), e))
            }
    }
}