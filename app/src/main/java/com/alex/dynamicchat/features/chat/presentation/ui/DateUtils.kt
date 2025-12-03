package com.alex.dynamicchat.features.chat.presentation.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val fullFormatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    fun formatTime(timestamp: Long): String = timeFormatter.format(Date(timestamp))

    fun formatDate(timestamp: Long): String = dateFormatter.format(Date(timestamp))

    fun formatFull(timestamp: Long): String = fullFormatter.format(Date(timestamp))
}
