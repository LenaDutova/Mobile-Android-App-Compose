package com.mobile.vedroid.compose.model

import kotlin.Int
import kotlin.String

data class Joke (
    val id: Int,
    val isSingle: Boolean,
    val setup: String,
    val delivery: String? = null){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Joke

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}