package com.alex.dynamicchat.features.chat.data.network.dto

import com.alex.dynamicchat.features.chat.domain.model.Message

data class MessageDto(
    val id: Long?,
    val senderId: String?,
    val senderName: String?,
    val text: String?,
    val timestamp: Long?,
    val isUnread: Boolean?
)


fun MessageDto.toDomain(currentUserId: String): Message {
    return Message(
        id = id ?: 0L,
        senderId = senderId.orEmpty(),
        senderName = senderName.orEmpty(),
        isMe = senderId == currentUserId,
        text = text.orEmpty(),
        timestamp = timestamp ?: System.currentTimeMillis(),
        isUnread = isUnread ?: false
    )
}

