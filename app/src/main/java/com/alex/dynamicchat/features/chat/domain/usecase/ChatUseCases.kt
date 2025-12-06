package com.alex.dynamicchat.features.chat.domain.usecase

import javax.inject.Inject

data class ChatUseCases @Inject constructor(
    val observeMessages: ObserveMessagesUseCase,
    val observeConnectionEvents: ObserveConnectionEventsUseCase,
    val connectToChat: ConnectToChatUseCase,
    val disconnectFromChat: DisconnectFromChatUseCase,
    val sendMessage: SendMessageUseCase
)