package com.alex.dynamicchat.features.chat.presentation.state

sealed class ChatState {
    data object Idle : ChatState()
    data object Sending : ChatState()
    data class Error(val message: String) : ChatState()
}