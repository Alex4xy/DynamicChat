package com.alex.dynamicchat.features.chat.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alex.dynamicchat.core.app.chatDataStore
import com.alex.dynamicchat.core.corotuine.DefaultDispatcher
import com.alex.dynamicchat.features.chat.presentation.ui.theme.modes.ChatLayoutMode
import com.alex.dynamicchat.features.chat.presentation.ui.theme.modes.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatPreferencesDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    companion object {
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val LAYOUT_MODE = stringPreferencesKey("layout_mode")
    }

    fun getThemeMode(): Flow<ThemeMode> {
        return context.chatDataStore.data.map { preferences ->
            val stored = preferences[THEME_MODE]
            stored?.let {
                runCatching { ThemeMode.valueOf(it) }.getOrNull()
            } ?: ThemeMode.DARK
        }
    }

    fun getLayoutMode(): Flow<ChatLayoutMode> {
        return context.chatDataStore.data.map { preferences ->
            val stored = preferences[LAYOUT_MODE]
            stored?.let {
                runCatching { ChatLayoutMode.valueOf(it) }.getOrNull()
            } ?: ChatLayoutMode.CLASSIC
        }
    }

    suspend fun saveThemeMode(mode: ThemeMode) {
        withContext(dispatcher) {
            context.chatDataStore.edit { prefs ->
                prefs[THEME_MODE] = mode.name
            }
        }
    }

    suspend fun saveLayoutMode(mode: ChatLayoutMode) {
        withContext(dispatcher) {
            context.chatDataStore.edit { prefs ->
                prefs[LAYOUT_MODE] = mode.name
            }
        }
    }
}
