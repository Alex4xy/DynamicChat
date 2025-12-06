package com.alex.dynamicchat.features.chat.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun formatTime(timestamp: Long): String = timeFormatter.format(Date(timestamp))
}