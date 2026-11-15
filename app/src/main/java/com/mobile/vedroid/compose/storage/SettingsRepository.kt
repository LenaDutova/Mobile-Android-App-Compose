package com.mobile.vedroid.compose.storage

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.mobile.vedroid.compose.storage.vaults.DSStore
import com.mobile.vedroid.compose.ui.theme.AppMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SettingsRepository {

    val SETTINGS_MODE = intPreferencesKey("mode")
    val SETTINGS_IS_ALWAYS_RU = booleanPreferencesKey("ru")

    suspend fun saveThemeMode (mode: AppMode) {
        DSStore.saveIntValue(SETTINGS_MODE, mode.index)
    }

    suspend fun saveOnlyRuLanguage(ru: Boolean) {
        DSStore.saveBooleanValue(SETTINGS_IS_ALWAYS_RU, ru)
    }

    fun loadThemeMode(): Flow<AppMode> = DSStore.loadIntValue(SETTINGS_MODE, AppMode.SYSTEM.index).map { it -> AppMode.entries.getOrNull(it) ?: AppMode.SYSTEM }

    fun loadOnlyRuLanguage(): Flow<Boolean> = DSStore.loadBooleanValue(SETTINGS_IS_ALWAYS_RU, false)
}