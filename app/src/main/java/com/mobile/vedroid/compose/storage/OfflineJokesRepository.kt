package com.mobile.vedroid.compose.storage

import com.mobile.vedroid.compose.model.Joke
import kotlinx.coroutines.flow.Flow

interface OfflineJokesRepository {

    fun load(): Flow<List<Joke>>

    suspend fun save(items: List<Joke>)
    suspend fun save(item: Joke)
}