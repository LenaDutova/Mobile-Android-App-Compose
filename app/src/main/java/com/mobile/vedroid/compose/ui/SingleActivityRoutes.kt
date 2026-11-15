package com.mobile.vedroid.compose.ui

import androidx.navigation3.runtime.NavKey
import com.mobile.vedroid.compose.model.UserAccount
import kotlinx.serialization.Serializable

sealed interface SingleActivityRoutes : NavKey {
    @Serializable
    data class Start(
        val user : UserAccount? = null
    ) : SingleActivityRoutes


    @Serializable
    data object Returning : SingleActivityRoutes

    @Serializable
    data object Settings : SingleActivityRoutes

    @Serializable
    data object Final : SingleActivityRoutes
}