package com.mobile.vedroid.compose.network

data class JokesRequest (
    val id: Int,
    val type: String,
    val joke: String? = null,
    val setup: String? = null,
    val delivery: String? = null) {

    fun isSingle () = type == "single"

    class ApiJokesList (val jokes: MutableList<JokesRequest>)
}