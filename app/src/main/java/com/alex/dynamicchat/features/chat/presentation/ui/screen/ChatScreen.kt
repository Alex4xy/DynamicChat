package com.alex.dynamicchat.features.chat.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.network.NetworkStatus
import com.alex.dynamicchat.core.providers.ResourceProvider
import com.alex.dynamicchat.features.chat.presentation.event.ChatEvent
import com.alex.dynamicchat.features.chat.presentation.state.ChatState
import com.alex.dynamicchat.features.chat.presentation.state.ChatEmptyState
import com.alex.dynamicchat.features.chat.presentation.ui.components.ChatTopBar
import com.alex.dynamicchat.features.chat.presentation.ui.layouts.beehive.BeehiveChatLayout
import com.alex.dynamicchat.features.chat.presentation.ui.layouts.classic.ClassicChatLayout
import com.alex.dynamicchat.features.chat.presentation.ui.layouts.compact.CompactChatLayout
import com.alex.dynamicchat.features.chat.presentation.ui.theme.modes.ChatLayoutMode
import com.alex.dynamicchat.features.chat.presentation.ui.theme.LocalChatThemeColors
import com.alex.dynamicchat.features.chat.presentation.ui.theme.chatColorsFor
import com.alex.dynamicchat.features.chat.presentation.ui.viewmodel.ChatViewModel
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

    val classicListState = rememberLazyListState()
    val compactListState = rememberLazyListState()
    val beehiveScrollState = rememberScrollState()

    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = viewModel.errorEvent) {
        viewModel.errorEvent.collect { snackBarHostState.showSnackbar(it) }
    }

    LaunchedEffect(messages.size, layoutMode) {
        if (messages.isNotEmpty()) {
            when (layoutMode) {
                ChatLayoutMode.CLASSIC -> {
                    classicListState.animateScrollToItem(messages.lastIndex)
                }

                ChatLayoutMode.COMPACT -> {
                    compactListState.animateScrollToItem(messages.lastIndex)
                }

                ChatLayoutMode.BEEHIVE -> {
                    beehiveScrollState.animateScrollTo(beehiveScrollState.maxValue)
                }
            }
        }
    }

    val chatColors = remember(themeMode) { chatColorsFor(themeMode) }

    CompositionLocalProvider(LocalChatThemeColors provides chatColors) {
        val colors = LocalChatThemeColors.current

        val isError = chatState is ChatState.Error
        val isNetworkAvailable = networkStatus == NetworkStatus.Available

        val canSend = inputText.isNotBlank() &&
                isNetworkAvailable &&
                !isError

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
                    isEnabled = canSend,
                    resourceProvider = resourceProvider,
                    onTextChange = { viewModel.onEvent(ChatEvent.MessageInputChanged(it)) },
                    onSendClicked = { viewModel.onEvent(ChatEvent.SendMessage) }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
            ) {
                if (messages.isEmpty()) {
                    ChatEmptyState(
                        resourceProvider = resourceProvider,
                        modifier = Modifier.padding(paddingValues)
                    )
                } else {
                    when (layoutMode) {
                        ChatLayoutMode.CLASSIC -> ClassicChatLayout(
                            messages = messages,
                            modifier = Modifier.fillMaxSize(),
                            listState = classicListState,
                            contentPadding = paddingValues
                        )

                        ChatLayoutMode.COMPACT -> CompactChatLayout(
                            messages = messages,
                            modifier = Modifier.fillMaxSize(),
                            listState = compactListState,
                            contentPadding = paddingValues
                        )

                        ChatLayoutMode.BEEHIVE -> BeehiveChatLayout(
                            messages = messages,
                            modifier = Modifier.fillMaxSize(),
                            scrollState = beehiveScrollState,
                            contentPadding = paddingValues
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageInputBar(
    text: String,
    isEnabled: Boolean,
    resourceProvider: ResourceProvider,
    onTextChange: (String) -> Unit,
    onSendClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalChatThemeColors.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
            .navigationBarsPadding()
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
                    color = colors.inputPlaceholder
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (isEnabled) {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        onSendClicked()
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary,
                cursorColor = colors.textPrimary,
                focusedBorderColor = colors.inputBorder,
                unfocusedBorderColor = colors.inputBorder.copy(alpha = 0.5f),
                focusedPlaceholderColor = colors.inputPlaceholder,
                unfocusedPlaceholderColor = colors.inputPlaceholder,
            )
        )
        Button(
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onSendClicked()
            },
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.buttonBackground,
                contentColor = colors.buttonText,
                disabledContainerColor = colors.buttonBackground.copy(alpha = 0.4f),
                disabledContentColor = colors.buttonText.copy(alpha = 0.4f),
            )
        ) {
            Text(
                text = resourceProvider.getString(R.string.chat_send),
                color = colors.buttonText
            )
        }
    }
}