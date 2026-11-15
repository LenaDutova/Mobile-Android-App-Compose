package com.mobile.vedroid.compose.storage

import com.mobile.vedroid.compose.model.UserAccount
import com.mobile.vedroid.compose.storage.vaults.SharedPreferencesStore

object AccountRepository {

    private const val ACCOUNT_NAME = "NAME"
    private const val ACCOUNT_SEX = "SEX"

    fun hasSavedAccount () : Boolean = SharedPreferencesStore.hasValue(ACCOUNT_NAME)

    fun loadAccount (): UserAccount? {
        return if (SharedPreferencesStore.hasValue(ACCOUNT_NAME)) UserAccount (
            name = SharedPreferencesStore.readValue(ACCOUNT_NAME, String) as String,
            gender = SharedPreferencesStore.readValue(ACCOUNT_SEX, Boolean) as Boolean )
        else null
    }

    fun saveAccount (account: UserAccount){
        SharedPreferencesStore.saveValue(ACCOUNT_NAME, account.name)
        SharedPreferencesStore.saveValue(ACCOUNT_SEX, account.gender)
    }

    fun clearAccount(): Unit {
        SharedPreferencesStore.clear()
    }
}