package com.alex.dynamicchat.features.chat.presentation.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.alex.dynamicchat.features.chat.presentation.ui.theme.modes.ThemeMode
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

    val inputBorder: Color,
    val inputPlaceholder: Color,
    val buttonBackground: Color,
    val buttonText: Color,
    val menuBackground: Color,
    val menuItemText: Color,

    val bubbleMeText: Color,
    val bubbleOtherText: Color,
    val bubbleMeTimestamp: Color,
    val bubbleOtherTimestamp: Color,

    val bubbleBorder: Color,
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
        unread = Color.Yellow,
        inputBorder = Color.LightGray,
        inputPlaceholder = Color.LightGray,
        buttonBackground = Color(0xFF1565C0),
        buttonText = Color.White,
        menuBackground = Color.DarkGray,
        menuItemText = Color.White,
        bubbleMeText = Color.White,
        bubbleOtherText = Color.White,
        bubbleMeTimestamp = Color.LightGray,
        bubbleOtherTimestamp = Color.LightGray,
        bubbleBorder = Color.Transparent,
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
            unread = Color(0xFFD32F2F),

            inputBorder = Color(0xFFBDBDBD),
            inputPlaceholder = Color(0xFF9E9E9E),
            buttonBackground = Color(0xFF1976D2),
            buttonText = Color.White,
            menuBackground = Color(0xFFFFFFFF),
            menuItemText = Color(0xFF212121),

            bubbleMeText = Color.White,
            bubbleOtherText = Color(0xFF212121),
            bubbleMeTimestamp = Color(0xFFE3F2FD),
            bubbleOtherTimestamp = Color(0xFF757575),

            bubbleBorder = Color.Transparent,
        )

        ThemeMode.DARK -> ChatThemeColors(
            background = DarkBackground,
            appBar = DeepBlue,
            appBarContent = White,
            textPrimary = White,
            textSecondary = White.copy(alpha = 0.7f),
            bubbleMe = DeepBlue,
            bubbleOther = StormGray,
            unread = Color.Yellow,

            inputBorder = White.copy(alpha = 0.7f),
            inputPlaceholder = White.copy(alpha = 0.7f),
            buttonBackground = DeepBlue,
            buttonText = White,
            menuBackground = StormGray,
            menuItemText = White,

            bubbleMeText = White,
            bubbleOtherText = White,
            bubbleMeTimestamp = White.copy(alpha = 0.8f),
            bubbleOtherTimestamp = White.copy(alpha = 0.8f),

            bubbleBorder = Color.Transparent,
        )

        ThemeMode.HIGH_CONTRAST -> ChatThemeColors(
            background = Color.Black,
            appBar = Color.Black,
            appBarContent = Color.Yellow,
            textPrimary = Color.White,
            textSecondary = Color.Yellow,
            bubbleMe = Color.Black,
            bubbleOther = Color.Black,
            unread = Color.Yellow,

            inputBorder = Color.Yellow,
            inputPlaceholder = Color.Yellow,
            buttonBackground = Color.Yellow,
            buttonText = Color.Black,
            menuBackground = Color.Black,
            menuItemText = Color.Yellow,

            bubbleMeText = Color.White,
            bubbleOtherText = Color.White,
            bubbleMeTimestamp = Color.Yellow,
            bubbleOtherTimestamp = Color.Yellow,

            bubbleBorder = Color.Yellow,
        )
    }
}

