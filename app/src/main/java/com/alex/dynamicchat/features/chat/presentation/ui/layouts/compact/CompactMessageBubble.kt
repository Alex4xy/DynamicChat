package com.alex.dynamicchat.features.chat.presentation.ui.layouts.compact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.ui.Dimensions.paddingSmall
import com.alex.dynamicchat.ui.theme.DeepBlue
import com.alex.dynamicchat.ui.theme.StormGray
import com.alex.dynamicchat.ui.theme.White

@Composable
fun CompactMessageBubble(
    message: MessageUi,
    showSenderHeader: Boolean,
    modifier: Modifier = Modifier
) {
    val bubbleColor = if (message.isMe) DeepBlue else StormGray
    val alignment =
        if (message.isMe) Arrangement.End else Arrangement.Start

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = paddingSmall, vertical = 4.dp)
        ) {
            if (showSenderHeader) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall,
                    color = White.copy(alpha = 0.7f)
                )
            }
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodySmall,
                color = White
            )
        }
    }
}