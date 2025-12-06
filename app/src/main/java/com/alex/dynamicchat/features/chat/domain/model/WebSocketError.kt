package com.alex.dynamicchat.features.chat.domain.model

sealed class WebSocketError {
    object NotConnected : WebSocketError()
    object NotReady : WebSocketError()
    object SendFailed : WebSocketError()
    object NullSocket : WebSocketError()
    data class Unknown(val throwable: Throwable?) : WebSocketError()
}