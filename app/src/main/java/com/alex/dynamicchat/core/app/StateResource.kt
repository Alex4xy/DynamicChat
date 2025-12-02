package com.alex.dynamicchat.core.app

sealed class StateResource<out T> {
    data class Success<T>(val data: T) : StateResource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : StateResource<Nothing>()
}