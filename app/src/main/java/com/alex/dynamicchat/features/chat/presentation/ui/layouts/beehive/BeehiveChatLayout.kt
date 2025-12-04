package com.alex.dynamicchat.features.chat.presentation.ui.layouts.beehive

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alex.dynamicchat.features.chat.presentation.ui.DateUtils
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.features.chat.presentation.ui.theme.LocalChatThemeColors
import com.alex.dynamicchat.ui.Dimensions.paddingMedium
import com.alex.dynamicchat.ui.Dimensions.paddingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeehiveChatLayout(
    messages: List<MessageUi>,
    modifier: Modifier = Modifier
) {
    var selectedMessage by remember { mutableStateOf<MessageUi?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.verticalScroll(rememberScrollState())) {
        BeehiveLayout(
            modifier = Modifier
                .padding(paddingMedium)
        ) {
            messages.forEach { message ->
                HexTile(
                    message = message,
                    onClick = { selectedMessage = message }
                )
            }
        }
    }

    selectedMessage?.let { message ->
        ModalBottomSheet(
            onDismissRequest = { selectedMessage = null },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingMedium)
            ) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(paddingSmall))

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(paddingSmall))

                Text(
                    text = DateUtils.formatTime(message.timestamp),
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.height(paddingMedium))
            }
        }
    }
}

@Composable
private fun HexTile(
    message: MessageUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalChatThemeColors.current
    val tileColor = if (message.isMe) colors.bubbleMe else colors.bubbleOther

    Box(
        modifier = modifier
            .size(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(tileColor)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message.senderName.take(6),
            style = MaterialTheme.typography.labelMedium,
            color = colors.textPrimary
        )
    }
}