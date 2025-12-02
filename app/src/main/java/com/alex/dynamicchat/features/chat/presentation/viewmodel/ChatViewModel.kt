package com.alex.dynamicchat.features.chat.presentation.viewmodel

import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.app.BaseViewModel
import com.alex.dynamicchat.core.corotuine.DefaultDispatcher
import com.alex.dynamicchat.core.corotuine.IoDispatcher
import com.alex.dynamicchat.core.corotuine.MainDispatcher
import com.alex.dynamicchat.core.network.NetworkObserver
import com.alex.dynamicchat.core.providers.ResourceProvider
import com.alex.dynamicchat.features.chat.presentation.event.ChatEvent
import com.alex.dynamicchat.features.chat.presentation.state.ChatState
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

    private fun sendMessage() {
        val text = _messageInput.value.trim()
        if (text.isEmpty()) return

        launchIoSafe {
            if (_state.value !is ChatState.Sending) {
                launchMainSafe { _state.value = ChatState.Sending }
            }

            try {
                // TODO: hook WebSocket client here later
                // messagingClient.sendMessage(text)

                launchMainSafe {
                    _messageInput.value = ""
                    _state.value = ChatState.Idle
                }
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: resourceProvider.getString(R.string.generic_unknown_error)

                launchMainSafe {
                    _state.value = ChatState.Error(errorMessage)
                }
            }
        }
    }
}

