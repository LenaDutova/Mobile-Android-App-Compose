package com.mobile.vedroid.compose.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JokeRetrofitAPI {

    // use callback in UI-thread
    @GET("joke/Programming?amount=5")
    fun callJokes(): Call<JokesRequest.ApiJokesList>

    // need background thread
    @GET("joke/Programming")
    suspend fun getJoke(): Response<JokesRequest>

    @GET("joke/Programming?amount=5")
    suspend fun getJokes(): Response<JokesRequest.ApiJokesList>

    @GET("joke/Programming")
    suspend fun getJokes(@Query("amount") count: Int): Response<JokesRequest.ApiJokesList>
}