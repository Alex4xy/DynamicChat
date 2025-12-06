package com.alex.dynamicchat.features.chat.presentation.ui.layouts.classic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.dynamicchat.features.chat.presentation.utils.DateUtils
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.features.chat.presentation.ui.theme.LocalChatThemeColors
import com.alex.dynamicchat.ui.Dimensions.paddingSmall

@Composable
fun ClassicMessageBubble(
    message: MessageUi,
    modifier: Modifier = Modifier
) {
    val colors = LocalChatThemeColors.current

    val isMe = message.isMe
    val bubbleColor = if (isMe) colors.bubbleMe else colors.bubbleOther
    val alignment = if (isMe) Arrangement.End else Arrangement.Start
    val nameColor = if (isMe) colors.bubbleMeText else colors.bubbleOtherText
    val textColor = nameColor
    val timeColor = if (isMe) colors.bubbleMeTimestamp else colors.bubbleOtherTimestamp

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .border(
                    width = 2.dp,
                    color = colors.bubbleBorder,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isMe) 0.dp else 16.dp,
                        bottomStart = if (isMe) 16.dp else 0.dp
                    )
                )
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isMe) 0.dp else 16.dp,
                        bottomStart = if (isMe) 16.dp else 0.dp
                    )
                )
                .padding(paddingSmall)
        ) {
            Text(
                text = message.senderName,
                style = MaterialTheme.typography.labelSmall,
                color = nameColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = DateUtils.formatTime(message.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = timeColor
                )
                if (message.isUnread && !message.isMe) {
                    Text(
                        text = "UNREAD",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.unread
                    )
                }
            }
        }
    }
}