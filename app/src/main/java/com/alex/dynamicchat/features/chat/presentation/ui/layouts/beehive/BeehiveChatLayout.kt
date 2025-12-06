package com.alex.dynamicchat.features.chat.presentation.ui.layouts.beehive

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alex.dynamicchat.features.chat.presentation.utils.DateUtils
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.features.chat.presentation.ui.theme.LocalChatThemeColors
import com.alex.dynamicchat.ui.Dimensions.paddingMedium
import com.alex.dynamicchat.ui.Dimensions.paddingSmall
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeehiveChatLayout(
    messages: List<MessageUi>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    var selectedMessage by remember { mutableStateOf<MessageUi?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        BeehiveLayout(
            messages = messages,
            modifier = Modifier.padding(16.dp)
        ) { msg ->
            HexTile(message = msg, onClick = { selectedMessage = msg })
        }
    }
    selectedMessage?.let { message ->
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    selectedMessage = null
                }
            }
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
    val isMe = message.isMe

    val backgroundColor = if (isMe) colors.bubbleMe else colors.bubbleOther
    val nameColor = if (isMe) colors.bubbleMeText else colors.bubbleOtherText
    val textColor = nameColor
    val timeColor = if (isMe) colors.bubbleMeTimestamp else colors.bubbleOtherTimestamp

    Box(
        modifier = modifier
            .size(150.dp)
            .border(2.dp, colors.bubbleBorder, HexagonShape)
            .clip(HexagonShape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Text(
            text = message.senderName,
            style = MaterialTheme.typography.labelSmall,
            color = nameColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 6.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodySmall,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = DateUtils.formatTime(message.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = timeColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (message.isUnread && !message.isMe) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-6).dp, y = 6.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colors.unread)
            )
        }
    }
}