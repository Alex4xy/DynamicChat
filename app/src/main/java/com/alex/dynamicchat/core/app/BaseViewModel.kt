package com.alex.dynamicchat.core.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.corotuine.DefaultDispatcher
import com.alex.dynamicchat.core.corotuine.IoDispatcher
import com.alex.dynamicchat.core.corotuine.MainDispatcher
import com.alex.dynamicchat.core.network.NetworkObserver
import com.alex.dynamicchat.core.network.NetworkStatus
import com.alex.dynamicchat.core.providers.ResourceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

open class BaseViewModel @Inject constructor(
    private val networkObserver: NetworkObserver,
    val resourceProvider: ResourceProvider,
    @param:MainDispatcher protected val mainDispatcher: CoroutineDispatcher,
    @param:IoDispatcher protected val ioDispatcher: CoroutineDispatcher,
    @param:DefaultDispatcher protected val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    protected val _networkStatus = MutableStateFlow(NetworkStatus.Unavailable)
    val networkStatus: StateFlow<NetworkStatus> = _networkStatus

    private val _errorEvent = MutableSharedFlow<String>(replay = 0)
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    init {
        launchIoSafe {
            networkObserver.observe().collect { status ->
                _networkStatus.value = status
            }
        }
    }

    protected fun launchMainSafe(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(mainDispatcher) {
            try {
                block()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                val errorMessage =
                    e.localizedMessage ?: resourceProvider.getString(R.string.generic_unknown_error)
                _errorEvent.tryEmit(errorMessage)
            }
        }
    }

    protected fun launchIoSafe(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            try {
                block()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                val errorMessage =
                    e.localizedMessage ?: resourceProvider.getString(R.string.generic_unknown_error)
                _errorEvent.tryEmit(errorMessage)
            }
        }
    }

    protected fun launchDefaultSafe(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                block()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                val errorMessage =
                    e.localizedMessage ?: resourceProvider.getString(R.string.generic_unknown_error)
                _errorEvent.tryEmit(errorMessage)
            }
        }
    }
}
