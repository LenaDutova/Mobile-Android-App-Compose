package com.mobile.vedroid.compose

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface SingleActivityRoutes : NavKey {
    @Serializable
    data class Start(
        val name: String? = null,
        val sex: Boolean = false,
    ) : SingleActivityRoutes

    @Serializable
    data object Returning : SingleActivityRoutes

    @Serializable
    data object Settings : SingleActivityRoutes

    @Serializable
    data object Final : SingleActivityRoutes
}