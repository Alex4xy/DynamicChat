package com.alex.dynamicchat.features.chat.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.providers.ResourceProvider
import com.alex.dynamicchat.features.chat.presentation.event.ChatEvent
import com.alex.dynamicchat.features.chat.presentation.state.ChatState
import com.alex.dynamicchat.features.chat.presentation.ui.ChatEmptyState
import com.alex.dynamicchat.features.chat.presentation.ui.components.ChatTopBar
import com.alex.dynamicchat.features.chat.presentation.ui.layouts.beehive.BeehiveChatLayout
import com.alex.dynamicchat.features.chat.presentation.ui.layouts.classic.ClassicChatLayout
import com.alex.dynamicchat.features.chat.presentation.ui.layouts.compact.CompactChatLayout
import com.alex.dynamicchat.features.chat.presentation.ui.modes.ChatLayoutMode
import com.alex.dynamicchat.features.chat.presentation.ui.theme.LocalChatThemeColors
import com.alex.dynamicchat.features.chat.presentation.ui.theme.chatColorsFor
import com.alex.dynamicchat.features.chat.presentation.viewmodel.ChatViewModel
import com.alex.dynamicchat.ui.Dimensions.paddingMedium
import com.alex.dynamicchat.ui.Dimensions.paddingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = hiltViewModel()
    val resourceProvider = viewModel.resourceProvider

    val networkStatus = viewModel.networkStatus.collectAsState().value
    val chatState by viewModel.state.collectAsState()
    val inputText by viewModel.messageInput.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val layoutMode by viewModel.layoutMode.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = viewModel.errorEvent) {
        viewModel.errorEvent.collect { snackBarHostState.showSnackbar(it) }
    }

    val chatColors = remember(themeMode) { chatColorsFor(themeMode) }

    CompositionLocalProvider(LocalChatThemeColors provides chatColors) {
        val colors = LocalChatThemeColors.current

        val isSending = chatState is ChatState.Sending

        Scaffold(
            containerColor = colors.background,
            topBar = {
                ChatTopBar(
                    resourceProvider = resourceProvider,
                    networkStatus = networkStatus,
                    layoutMode = layoutMode,
                    themeMode = themeMode,
                    onLayoutModeChanged = viewModel::onLayoutModeChanged,
                    onThemeModeChanged = viewModel::onThemeModeChanged
                )
            },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            bottomBar = {
                MessageInputBar(
                    text = inputText,
                    isSending = isSending,
                    resourceProvider = resourceProvider,
                    onTextChange = { viewModel.onEvent(ChatEvent.MessageInputChanged(it)) },
                    onSendClicked = { viewModel.onEvent(ChatEvent.SendMessage) }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colors.background)
            ) {
                if (messages.isEmpty()) {
                    ChatEmptyState(resourceProvider = resourceProvider)
                } else {
                    when (layoutMode) {
                        ChatLayoutMode.CLASSIC -> ClassicChatLayout(messages, Modifier.fillMaxSize())
                        ChatLayoutMode.COMPACT -> CompactChatLayout(messages, Modifier.fillMaxSize())
                        ChatLayoutMode.BEEHIVE -> BeehiveChatLayout(messages, Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
fun MessageInputBar(
    text: String,
    isSending: Boolean,
    resourceProvider: ResourceProvider,
    onTextChange: (String) -> Unit,
    onSendClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalChatThemeColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = paddingMedium, vertical = paddingSmall),
        horizontalArrangement = Arrangement.spacedBy(paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    text = resourceProvider.getString(R.string.chat_input_placeholder),
                    color = colors.textSecondary
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary,
                cursorColor = colors.textPrimary,
                focusedBorderColor = colors.textSecondary,
                unfocusedBorderColor = colors.textSecondary.copy(alpha = 0.5f),
                focusedPlaceholderColor = colors.textSecondary,
                unfocusedPlaceholderColor = colors.textSecondary
            )
        )
        Button(
            onClick = onSendClicked,
            enabled = text.isNotBlank() && !isSending
        ) {
            Text(
                text = resourceProvider.getString(R.string.chat_send),
                color = colors.textPrimary
            )
        }
    }
}