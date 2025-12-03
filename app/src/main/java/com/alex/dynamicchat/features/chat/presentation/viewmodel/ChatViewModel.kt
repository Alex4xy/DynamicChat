package com.alex.dynamicchat.features.chat.presentation.viewmodel

import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.app.BaseViewModel
import com.alex.dynamicchat.core.corotuine.DefaultDispatcher
import com.alex.dynamicchat.core.corotuine.IoDispatcher
import com.alex.dynamicchat.core.corotuine.MainDispatcher
import com.alex.dynamicchat.core.network.NetworkObserver
import com.alex.dynamicchat.core.providers.ResourceProvider
import com.alex.dynamicchat.features.chat.domain.model.Message
import com.alex.dynamicchat.features.chat.presentation.event.ChatEvent
import com.alex.dynamicchat.features.chat.presentation.state.ChatState
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.features.chat.presentation.ui.models.toUi
import com.alex.dynamicchat.features.chat.presentation.ui.modes.ChatLayoutMode
import com.alex.dynamicchat.features.chat.presentation.ui.modes.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ChatViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    networkObserver: NetworkObserver,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : BaseViewModel(
    networkObserver = networkObserver,
    resourceProvider = resourceProvider,
    mainDispatcher = mainDispatcher,
    ioDispatcher = ioDispatcher,
    defaultDispatcher = defaultDispatcher
) {

    private val _state = MutableStateFlow<ChatState>(ChatState.Idle)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _messageInput = MutableStateFlow("")
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageUi>>(emptyList())
    val messages: StateFlow<List<MessageUi>> = _messages.asStateFlow()

    private val _layoutMode = MutableStateFlow(ChatLayoutMode.CLASSIC)
    val layoutMode: StateFlow<ChatLayoutMode> = _layoutMode.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.DARK) // default for now
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    init {
        // Temporary demo messages so UI doesn’t look empty
        val now = System.currentTimeMillis()
        val demoDomainMessages = listOf(
            Message(
                id = 1L,
                senderId = "me",
                senderName = "You",
                isMe = true,
                text = "Hey, this is a sample message.",
                timestamp = now - 60_000,
                isUnread = false
            ),
            Message(
                id = 2L,
                senderId = "bot",
                senderName = "Compose Bot",
                isMe = false,
                text = "Hello! I’m another sample message.",
                timestamp = now - 30_000,
                isUnread = true
            ),
            Message(
                id = 3L,
                senderId = "bot",
                senderName = "Compose Bot",
                isMe = false,
                text = "We’ll later replace this with real WebSocket data.",
                timestamp = now - 10_000,
                isUnread = true
            )
        )

        _messages.value = demoDomainMessages.map { it.toUi() }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageInputChanged -> {
                _messageInput.value = event.text
            }
            ChatEvent.SendMessage -> {
                sendMessage()
            }
        }
    }

    fun onLayoutModeChanged(mode: ChatLayoutMode) {
        _layoutMode.value = mode
    }

    fun onThemeModeChanged(mode: ThemeMode) {
        _themeMode.value = mode
    }

    private fun sendMessage() {
        val text = _messageInput.value.trim()
        if (text.isEmpty()) return

        launchIoSafe {
            if (_state.value !is ChatState.Sending) {
                launchMainSafe { _state.value = ChatState.Sending }
            }

            try {
                // TODO: hook WebSocket client here later
                  launchMainSafe {
                    _messageInput.value = ""
                    _state.value = ChatState.Idle
                }
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage
                    ?: resourceProvider.getString(R.string.generic_unknown_error)

                launchMainSafe {
                    _state.value = ChatState.Error(errorMessage)
                }
            }
        }
    }
}

