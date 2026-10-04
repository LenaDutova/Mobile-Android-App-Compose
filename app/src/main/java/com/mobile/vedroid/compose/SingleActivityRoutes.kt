package com.mobile.vedroid.compose

import kotlinx.serialization.Serializable

sealed class SingleActivityRoutes{
    @Serializable
    data class Start (val name: String? = null, val sex: Boolean? = false) : SingleActivityRoutes ()

    @Serializable
    object Returning : SingleActivityRoutes ()

    @Serializable
    object Settings : SingleActivityRoutes ()

    @Serializable
    object Final : SingleActivityRoutes ()
}