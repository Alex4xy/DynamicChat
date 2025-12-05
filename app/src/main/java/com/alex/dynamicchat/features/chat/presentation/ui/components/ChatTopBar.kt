package com.alex.dynamicchat.features.chat.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            ChatLayoutMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (mode == currentMode) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = colors.textPrimary
                                )
                            } else {
                                Spacer(Modifier.size(24.dp))
                            }

                            Text(
                                text = mode.name,
                                fontWeight = if (mode == currentMode) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    }
                )
            }
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
            ThemeMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (mode == currentMode) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = colors.textPrimary
                                )
                            } else {
                                Spacer(Modifier.size(24.dp))
                            }

                            Text(
                                text = mode.name,
                                fontWeight = if (mode == currentMode) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}