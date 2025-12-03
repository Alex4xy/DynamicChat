package com.alex.dynamicchat.features.chat.presentation.ui.models

import com.alex.dynamicchat.features.chat.domain.model.Message

data class MessageUi(
    val id: Long,
    val senderName: String,
    val isMe: Boolean,
    val text: String,
    val timestamp: Long,
    val isUnread: Boolean
)


fun Message.toUi(): MessageUi {
    return MessageUi(
        id = id,
        senderName = senderName,
        isMe = isMe,
        text = text,
        timestamp = timestamp,
        isUnread = isUnread
    )
}

