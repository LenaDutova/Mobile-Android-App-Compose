package com.mobile.vedroid.compose.model.extensions

import com.mobile.vedroid.compose.model.Joke
import com.mobile.vedroid.compose.network.JokesRequest

fun JokesRequest.parseToJoke() : Joke {
    return Joke(
        id,
        isSingle(),
        if (isSingle()) joke!! else setup!!,
        delivery
    )
}