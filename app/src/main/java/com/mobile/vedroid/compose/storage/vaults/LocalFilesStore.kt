package com.mobile.vedroid.compose.storage.vaults

import android.content.Context
import android.util.Log
import com.mobile.vedroid.compose.MobileApplication
import com.mobile.vedroid.compose.model.Joke
import com.mobile.vedroid.compose.storage.OfflineJokesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Store in Internal storage vault:
 * Device Manager - ⋮ (Addition menu) - Open In Device Explorer
 * data / data / your namespace / files
 */
class LocalFilesStore : OfflineJokesRepository {

    companion object {
        const val FILE_NAME: String = "backup.txt"
    }

    override fun load(): Flow<List<Joke>> = flow {
        val jokes = mutableListOf<Joke>()

        if (File(MobileApplication.context().filesDir, FILE_NAME).exists()) {
            try {
                File(
                    MobileApplication.context().filesDir,
                    FILE_NAME
                ).forEachLine {
                    if (!Joke.JokeBuilder.parseToJokeAttr(it)) jokes.add(Joke.JokeBuilder.create())
                }
            } catch (e: IOException) {
                Log.e("VEDRO_ID", e.message, e)
            }
        }
        emit(jokes)
    }.flowOn(Dispatchers.IO)

    override suspend fun save(items: List<Joke>) {
        try {
            MobileApplication.context()
                .openFileOutput(FILE_NAME, Context.MODE_APPEND)
                .use { fos: FileOutputStream ->
                    items.forEach { joke ->
                        fos.write(joke.toString().toByteArray())
                    }
                }
        } catch (e: FileNotFoundException){
            Log.e("VEDRO_ID", e.message, e)
        } catch (e: IOException){
            Log.e("VEDRO_ID", e.message, e)
        }
    }

    override suspend fun save(item: Joke){
        try {
            MobileApplication.context()
                .openFileOutput(FILE_NAME, Context.MODE_APPEND)
                .use { fos: FileOutputStream ->
                    fos.write(item.toString().toByteArray())
                }
        } catch (e: FileNotFoundException){
            Log.e("VEDRO_ID", e.message, e)
        } catch (e: IOException){
            Log.e("VEDRO_ID", e.message, e)
        }
    }
}