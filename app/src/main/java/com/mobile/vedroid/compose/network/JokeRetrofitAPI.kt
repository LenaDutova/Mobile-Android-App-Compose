package com.mobile.vedroid.compose.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JokeRetrofitAPI {

    @GET("joke/Programming")
    fun getJoke(): Call<JokesRequest>

    @GET("joke/Programming?amount=10")
    fun getJokes(): Call<JokesRequest.ApiJokesList>

    @GET("joke/Programming?amount={count}}")
    fun getJokes(@Path("count") count: Int): Call<JokesRequest.ApiJokesList>
}