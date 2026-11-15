package com.mobile.vedroid.compose.storage.vaults

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.mobile.vedroid.compose.MobileApplication



object SharedPreferencesStore {

    private val sp: SharedPreferences by lazy {
        val ctx = MobileApplication.context()
        ctx.getSharedPreferences("SP", Context.MODE_PRIVATE)
    }

    fun clear () = sp.edit { clear() }

    fun hasValue (key : String) : Boolean = sp.contains(key)

    fun readValue (key : String, valueClass : Any): Any? = when (valueClass) {
        Boolean -> sp.getBoolean(key, false)
        String -> sp.getString(key, null)
        else -> {
            Log.d ( "VEDRO_ID", "SPStore: cannot readValue of this type ${valueClass}")
            null
        }
    }

    fun saveValue (key : String, value : Any) = when (value) {
        is Boolean -> sp.edit {putBoolean(key, value) }
        is String -> sp.edit { putString(key, value) }
        else -> Log.d ( "VEDRO_ID", "SPStore: cannot saveValue of this type ${value.javaClass}" )
    }
}