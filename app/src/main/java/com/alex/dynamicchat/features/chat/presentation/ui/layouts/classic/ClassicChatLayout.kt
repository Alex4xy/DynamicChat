package com.alex.dynamicchat.features.chat.presentation.ui.layouts.classic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.ui.Dimensions.paddingMedium
import com.alex.dynamicchat.ui.Dimensions.paddingSmall

@Composable
fun ClassicChatLayout(
    messages: List<MessageUi>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = paddingMedium, vertical = paddingSmall),
        verticalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {
        items(items = messages, key = { it.id }) { message ->
            ClassicMessageBubble(message = message)
        }
    }
}