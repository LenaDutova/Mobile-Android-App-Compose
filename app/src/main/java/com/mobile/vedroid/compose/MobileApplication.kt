package com.mobile.vedroid.compose

import android.app.Application
import android.content.Context
import com.mobile.vedroid.compose.storage.OfflineJokesRepository
import com.mobile.vedroid.compose.storage.vaults.LocalFilesStore

class MobileApplication : Application() {

    init {
        instance = this
    }

    companion object {

        private const val IS_FILE = true
        private var instance : MobileApplication? = null

        fun context() : Context {
            return instance!!.applicationContext
        }

        fun jokesRepository() : OfflineJokesRepository {
            return /* if (IS_FILE) */ LocalFilesStore() /* else SQLiteStore */
        }
    }
}