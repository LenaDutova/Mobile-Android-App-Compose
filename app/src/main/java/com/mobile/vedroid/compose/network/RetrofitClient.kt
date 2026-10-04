package com.mobile.vedroid.compose.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL = "https://v2.jokeapi.dev/"

    val apiService: JokeRetrofitAPI
        get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JokeRetrofitAPI::class.java)
}