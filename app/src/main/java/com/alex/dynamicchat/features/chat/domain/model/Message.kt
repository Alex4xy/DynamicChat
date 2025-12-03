package com.alex.dynamicchat.features.chat.domain.model

data class Message(
    val id: Long,
    val senderId: String,
    val senderName: String,
    val isMe: Boolean,
    val text: String,
    val timestamp: Long,
    val isUnread: Boolean
)