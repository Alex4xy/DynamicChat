package com.alex.dynamicchat.features.chat.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.providers.ResourceProvider
import com.alex.dynamicchat.features.chat.presentation.ui.theme.LocalChatThemeColors
import com.alex.dynamicchat.ui.Dimensions.paddingMedium

@Composable
fun ChatEmptyState(
    resourceProvider: ResourceProvider,
    modifier: Modifier = Modifier
) {
    val colors = LocalChatThemeColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingMedium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = resourceProvider.getString(R.string.chat_empty_state),
            color = colors.textSecondary
        )
    }
}
