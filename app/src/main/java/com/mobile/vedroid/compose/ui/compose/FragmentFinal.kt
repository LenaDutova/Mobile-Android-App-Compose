package com.mobile.vedroid.compose.ui.compose

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobile.vedroid.compose.R
import com.mobile.vedroid.compose.network.JokesRequest
import com.mobile.vedroid.compose.network.RetrofitClient
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.emptyList

@Composable
fun FragmentFinal(){
    val jokes = remember { mutableStateListOf<JokesRequest>() }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
    ) {
        // try load data
        RetrofitClient.apiService.getJokes().enqueue(object: Callback<JokesRequest.ApiJokesList> {
            override fun onFailure(call: Call<JokesRequest.ApiJokesList>, t: Throwable) {
                t.message?.let { Log.d("TAG", it) }
            }

            override fun onResponse(call: Call<JokesRequest.ApiJokesList>, response: Response<JokesRequest.ApiJokesList>) {
                if (response.isSuccessful){
                    val list = response.body()?.jokes as MutableList<JokesRequest>
                    list.forEach {
                        Log.d("TAG", it.toString())
                        jokes.add(it)
                    }
                } else {
                    Log.d("TAG", "" + response.code())
                }
            }
        })

        ShowJokeItems(jokes)
    }
}

@Composable
private fun ShowJokeItems (jokes: MutableList<JokesRequest>){
    if (jokes.isEmpty()) {
        Text(
            text = stringResource(id = R.string.warning_text_no_data),

            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            jokes.forEach {
                item { ItemJoke(
                    isSingle = it.isSingle(),
                    setup = if (it.isSingle()) it.joke!! else it.setup!!,
                    delivery = it.delivery)
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light"
)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
private fun PreviewFragmentFinal (){
    val mock = mutableListOf("Java", "Kotlin", "HTML", "CSS", "JavaScript", "SQL", "Python",  "C++", "Assembler", "Pascal", "Prolog", "Lisp", "C#")
    val jokes = mutableListOf<JokesRequest>()
    mock.forEach { jokes.add(JokesRequest(0, "single", it)) }

    MobileAndroidAppComposeTheme (dynamicColor = false) {
        ShowJokeItems(jokes)
//            JokeItems(emptyList())
//            JokeItems()
    }
}