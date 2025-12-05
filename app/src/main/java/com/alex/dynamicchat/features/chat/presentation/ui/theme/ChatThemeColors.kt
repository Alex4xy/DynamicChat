package com.alex.dynamicchat.features.chat.presentation.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.alex.dynamicchat.features.chat.presentation.ui.modes.ThemeMode
import com.alex.dynamicchat.ui.theme.DarkBackground
import com.alex.dynamicchat.ui.theme.DeepBlue
import com.alex.dynamicchat.ui.theme.StormGray
import com.alex.dynamicchat.ui.theme.White

@Immutable
data class ChatThemeColors(
    val background: Color,
    val appBar: Color,
    val appBarContent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val bubbleMe: Color,
    val bubbleOther: Color,
    val unread: Color,
)

val LocalChatThemeColors = compositionLocalOf {
    ChatThemeColors(
        background = Color.Black,
        appBar = Color.DarkGray,
        appBarContent = Color.White,
        textPrimary = Color.White,
        textSecondary = Color.LightGray,
        bubbleMe = Color(0xFF1565C0),
        bubbleOther = Color(0xFF424242),
        unread = Color.Yellow
    )
}

fun chatColorsFor(themeMode: ThemeMode): ChatThemeColors {
    return when (themeMode) {
        ThemeMode.LIGHT -> ChatThemeColors(
            background = Color(0xFFF5F5F5),
            appBar = Color(0xFFFFFFFF),
            appBarContent = Color(0xFF212121),
            textPrimary = Color(0xFF212121),
            textSecondary = Color(0xFF616161),
            bubbleMe = Color(0xFF1976D2),
            bubbleOther = Color(0xFFE0E0E0),
            unread = Color(0xFFD32F2F)
        )

        ThemeMode.DARK -> ChatThemeColors(
            background = DarkBackground,
            appBar = DeepBlue,
            appBarContent = White,
            textPrimary = White,
            textSecondary = White.copy(alpha = 0.7f),
            bubbleMe = DeepBlue,
            bubbleOther = StormGray,
            unread = Color.Yellow
        )

        ThemeMode.HIGH_CONTRAST -> ChatThemeColors(
            background = Color.Black,
            appBar = Color.Black,
            appBarContent = Color.Yellow,
            textPrimary = Color.White,
            textSecondary = Color.Yellow,
            bubbleMe = Color.Black,
            bubbleOther = Color.Black,
            unread = Color.Yellow
        )
    }
}
