package com.alex.dynamicchat.features.chat.presentation.ui.layouts.compact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.ui.Dimensions.paddingMedium
import com.alex.dynamicchat.ui.Dimensions.paddingSmall

@Composable
fun CompactChatLayout(
    messages: List<MessageUi>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = paddingMedium, vertical = paddingSmall),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        itemsIndexed(
            items = messages,
            key = { _, msg -> msg.id }
        ) { index, message ->
            val previousSenderId = messages.getOrNull(index - 1)?.id
            val showSenderHeader = previousSenderId != message.id

            CompactMessageBubble(
                message = message,
                showSenderHeader = showSenderHeader
            )
        }
    }
}