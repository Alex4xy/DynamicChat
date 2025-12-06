package com.alex.dynamicchat.features.chat.presentation.ui.viewmodel

import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.app.BaseViewModel
import com.alex.dynamicchat.core.app.StateResource
import com.alex.dynamicchat.core.corotuine.DefaultDispatcher
import com.alex.dynamicchat.core.corotuine.IoDispatcher
import com.alex.dynamicchat.core.corotuine.MainDispatcher
import com.alex.dynamicchat.core.network.NetworkObserver
import com.alex.dynamicchat.core.network.NetworkStatus
import com.alex.dynamicchat.core.providers.ResourceProvider
import com.alex.dynamicchat.features.chat.data.local.ChatPreferencesDataStore
import com.alex.dynamicchat.features.chat.domain.model.ConnectionEvent
import com.alex.dynamicchat.features.chat.domain.model.WebSocketError
import com.alex.dynamicchat.features.chat.domain.model.Message
import com.alex.dynamicchat.features.chat.domain.usecase.ChatUseCases
import com.alex.dynamicchat.features.chat.presentation.event.ChatEvent
import com.alex.dynamicchat.features.chat.presentation.state.ChatState
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi
import com.alex.dynamicchat.features.chat.presentation.ui.models.toUi
import com.alex.dynamicchat.features.chat.presentation.ui.theme.modes.ChatLayoutMode
import com.alex.dynamicchat.features.chat.presentation.ui.theme.modes.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val chatPreferencesDataStore: ChatPreferencesDataStore,
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

    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val currentUserId = "me"

    init {
        chatUseCases.connectToChat()

        observeConnectionEvents()
        observeMessages()
        observeNetwork()
        observeTheme()
        observeLayoutMode()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageInputChanged -> _messageInput.value = event.text
            ChatEvent.SendMessage -> sendMessage()
        }
    }

    fun onLayoutModeChanged(mode: ChatLayoutMode) {
        _layoutMode.value = mode
        launchIoSafe {
            chatPreferencesDataStore.saveLayoutMode(mode)
        }
    }

    fun onThemeModeChanged(mode: ThemeMode) {
        _themeMode.value = mode
        launchIoSafe {
            chatPreferencesDataStore.saveThemeMode(mode)
        }
    }

    private fun observeConnectionEvents() {
        launchIoSafe {
            chatUseCases.observeConnectionEvents().collect { event ->
                when (event) {
                    is ConnectionEvent.Connecting -> {
                        launchMainSafe { _state.value = ChatState.Idle }
                    }

                    is ConnectionEvent.Connected -> {
                        launchMainSafe { _state.value = ChatState.Idle }
                    }

                    is ConnectionEvent.Error -> {
                        val msg = when (val error = event.error) {
                            WebSocketError.NotConnected,
                            WebSocketError.NotReady -> {
                                resourceProvider.getString(R.string.error_connection_not_ready)
                            }

                            WebSocketError.SendFailed -> {
                                resourceProvider.getString(R.string.error_connection_send_failed)
                            }

                            WebSocketError.NullSocket -> {
                                resourceProvider.getString(R.string.error_connection_generic)
                            }

                            is WebSocketError.Unknown -> {
                                error.throwable?.localizedMessage
                                    ?: resourceProvider.getString(R.string.error_connection_generic)
                            }
                        }

                        launchMainSafe {
                            _state.value = ChatState.Error(msg)
                        }
                    }

                    is ConnectionEvent.Closed -> {
                        val reason = event.reason

                        val msg: String = if (!reason.isNullOrBlank()) {
                            resourceProvider.getString(
                                R.string.error_connection_closed_reason,
                                reason
                            )
                        } else {
                            resourceProvider.getString(R.string.error_connection_closed)
                        }

                        launchMainSafe {
                            _state.value = ChatState.Error(msg)
                        }
                    }

                }
            }
        }
    }

    private fun observeMessages() {
        launchIoSafe {
            chatUseCases.observeMessages().collect { result ->
                when (result) {
                    is StateResource.Success -> {
                        val ui = result.data.toUi()
                        launchMainSafe {
                            _messages.value = _messages.value + ui
                        }
                    }

                    is StateResource.Error -> {
                        launchMainSafe {
                            _state.value = ChatState.Error(result.message)
                        }
                    }
                }
            }
        }
    }

    private fun observeNetwork() {
        launchIoSafe {
            networkStatus.collect { status ->
                when (status) {
                    NetworkStatus.Available -> {
                        chatUseCases.connectToChat()
                    }

                    NetworkStatus.Unavailable,
                    NetworkStatus.Lost -> {
                        launchMainSafe {
                            _state.value = ChatState.Error(
                                resourceProvider.getString(R.string.error_network)
                            )
                        }
                        chatUseCases.disconnectFromChat()
                    }

                    NetworkStatus.Losing -> Unit
                }
            }
        }
    }

    private fun observeTheme() {
        launchIoSafe {
            chatPreferencesDataStore.getThemeMode().collect { mode ->
                launchMainSafe {
                    _themeMode.value = mode
                }
            }
        }
    }

    private fun observeLayoutMode() {
        launchIoSafe {
            chatPreferencesDataStore.getLayoutMode().collect { mode ->
                launchMainSafe {
                    _layoutMode.value = mode
                }
            }
        }
    }

    private fun sendMessage() {
        val text = _messageInput.value.trim()
        if (text.isEmpty()) return

        launchIoSafe {
            if (_state.value !is ChatState.Sending) {
                launchMainSafe { _state.value = ChatState.Sending }
            }

            val now = System.currentTimeMillis()

            val message = Message(
                id = now,
                senderId = currentUserId,
                senderName = "You",
                isMe = true,
                text = text,
                timestamp = now,
                isUnread = false
            )

            launchMainSafe {
                _messages.value = _messages.value + message.toUi()
            }

            when (val result = chatUseCases.sendMessage(message)) {
                is StateResource.Success -> {
                    launchMainSafe {
                        _messageInput.value = ""
                        _state.value = ChatState.Idle
                    }
                }

                is StateResource.Error -> {
                    launchMainSafe {
                        _state.value = ChatState.Error(result.message)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatUseCases.disconnectFromChat()
    }
}
