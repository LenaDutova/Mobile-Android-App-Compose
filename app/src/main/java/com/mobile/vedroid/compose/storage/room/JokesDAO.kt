package com.mobile.vedroid.compose.storage.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.vedroid.compose.model.Joke
import kotlinx.coroutines.flow.Flow

@Dao
interface JokesDAO {
    @Query("SELECT * FROM jokes")
    fun getJokes(): Flow<List<Joke>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(joke: Joke)

    @Delete
    suspend fun deleteAll(jokes: List<Joke>)
}