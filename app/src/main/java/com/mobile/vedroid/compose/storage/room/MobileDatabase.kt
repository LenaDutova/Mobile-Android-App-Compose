package com.mobile.vedroid.compose.storage.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobile.vedroid.compose.MobileApplication
import com.mobile.vedroid.compose.model.Joke

@Database(entities = [Joke::class], version = 1)
abstract class MobileDatabase : RoomDatabase() {

    abstract fun jokesDAO() : JokesDAO

    companion object {
        val db = Room.databaseBuilder(
            MobileApplication.context(),
            MobileDatabase::class.java, "DB"
        ).build()
    }
}