package com.alex.dynamicchat.features.chat.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.alex.dynamicchat.R
import com.alex.dynamicchat.core.network.NetworkStatus
import com.alex.dynamicchat.core.providers.ResourceProvider
import com.alex.dynamicchat.features.chat.presentation.ui.modes.ChatLayoutMode
import com.alex.dynamicchat.features.chat.presentation.ui.modes.ThemeMode
import com.alex.dynamicchat.features.chat.presentation.ui.theme.LocalChatThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    resourceProvider: ResourceProvider,
    networkStatus: NetworkStatus,
    layoutMode: ChatLayoutMode,
    themeMode: ThemeMode,
    onLayoutModeChanged: (ChatLayoutMode) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit
) {
    val colors = LocalChatThemeColors.current

    TopAppBar(
        title = {
            Column {
                Text(
                    text = resourceProvider.getString(R.string.title_chat),
                    color = colors.appBarContent
                )
                Text(
                    text = resourceProvider.getString(
                        R.string.status_network,
                        networkStatus
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.appBar,
            titleContentColor = colors.appBarContent,
            actionIconContentColor = colors.appBarContent
        ),
        actions = {
            LayoutModeMenu(
                currentMode = layoutMode,
                onModeSelected = onLayoutModeChanged
            )
            ThemeModeMenu(
                currentMode = themeMode,
                onModeSelected = onThemeModeChanged
            )
        }
    )
}

@Composable
private fun LayoutModeMenu(
    currentMode: ChatLayoutMode,
    onModeSelected: (ChatLayoutMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = LocalChatThemeColors.current
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.ViewAgenda,
                contentDescription = "Change layout",
                tint = colors.appBarContent
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Classic") },
                onClick = {
                    onModeSelected(ChatLayoutMode.CLASSIC)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Compact") },
                onClick = {
                    onModeSelected(ChatLayoutMode.COMPACT)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Beehive") },
                onClick = {
                    onModeSelected(ChatLayoutMode.BEEHIVE)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun ThemeModeMenu(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = LocalChatThemeColors.current
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.ColorLens,
                contentDescription = "Change theme",
                tint = colors.appBarContent
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Light") },
                onClick = {
                    onModeSelected(ThemeMode.LIGHT)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Dark") },
                onClick = {
                    onModeSelected(ThemeMode.DARK)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("High contrast") },
                onClick = {
                    onModeSelected(ThemeMode.HIGH_CONTRAST)
                    expanded = false
                }
            )
        }
    }
}