package com.alex.dynamicchat.features.chat.domain.model

sealed class ConnectionEvent {
    object Connecting : ConnectionEvent()
    object Connected : ConnectionEvent()
    data class Error(val error: WebSocketError) : ConnectionEvent()
    data class Closed(val reason: String?) : ConnectionEvent()
}
