package com.mobile.vedroid.compose.storage.room

import com.mobile.vedroid.compose.model.Joke
import com.mobile.vedroid.compose.storage.OfflineJokesRepository
import kotlinx.coroutines.flow.Flow

class SQLiteStore : OfflineJokesRepository {

    override fun load(): Flow<List<Joke>> = MobileDatabase.db.jokesDAO().getJokes()

    override suspend fun save(items: List<Joke>) {
        items.forEach {
                model -> MobileDatabase.db.jokesDAO().insert(model)
        }
    }

    override suspend fun save(item: Joke) {
        MobileDatabase.db.jokesDAO().insert(item)
    }
}