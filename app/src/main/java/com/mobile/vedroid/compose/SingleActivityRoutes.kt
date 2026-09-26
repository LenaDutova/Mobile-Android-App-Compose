package com.mobile.vedroid.compose

import kotlinx.serialization.Serializable

sealed class SingleActivityRoutes{
    @Serializable
    data class Start (val name: String? = null, val sex: Boolean? = false)

    @Serializable
    object Returning

    @Serializable
    object Settings

    @Serializable
    object Final
}