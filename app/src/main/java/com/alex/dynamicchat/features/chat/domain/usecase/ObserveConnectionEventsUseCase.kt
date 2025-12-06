package com.alex.dynamicchat.features.chat.domain.usecase

import com.alex.dynamicchat.features.chat.domain.model.ConnectionEvent
import com.alex.dynamicchat.features.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionEventsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<ConnectionEvent> =
        repository.connectionEvents
}