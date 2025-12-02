package com.alex.dynamicchat.features.chat.presentation.event

sealed class ChatEvent {
    data class MessageInputChanged(val text: String) : ChatEvent()
    data object SendMessage : ChatEvent()
}