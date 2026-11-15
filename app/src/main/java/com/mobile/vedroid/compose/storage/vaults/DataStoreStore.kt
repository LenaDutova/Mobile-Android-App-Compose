package com.mobile.vedroid.compose.storage.vaults

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mobile.vedroid.compose.MobileApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DSStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DS")

    suspend fun saveIntValue (key: Preferences.Key<Int>, value: Int) {
        MobileApplication.context().dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[key] = value
            }
        }
    }

    suspend fun saveBooleanValue (key: Preferences.Key<Boolean>, value: Boolean) {
        MobileApplication.context().dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[key] = value
            }
        }
    }

    fun loadIntValue (key: Preferences.Key <Int> , default : Int): Flow<Int> {
        return MobileApplication.context().dataStore.data.map { preferences ->
            preferences[key] ?: default
        }
    }

    fun loadBooleanValue (key: Preferences.Key <Boolean> , default : Boolean): Flow<Boolean> {
        return MobileApplication.context().dataStore.data.map { preferences ->
            preferences[key] ?: default
        }
    }

}