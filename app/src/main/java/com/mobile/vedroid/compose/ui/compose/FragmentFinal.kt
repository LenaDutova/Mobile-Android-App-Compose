package com.mobile.vedroid.compose.ui.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobile.vedroid.compose.R
import com.mobile.vedroid.compose.model.Joke
import com.mobile.vedroid.compose.model.extensions.parseToJoke
import com.mobile.vedroid.compose.network.JokesRequest
import com.mobile.vedroid.compose.network.RetrofitClient
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun FragmentFinal(){
    val jokes = rememberSaveable { mutableStateSetOf<Joke>() }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope  = rememberCoroutineScope()
    val snackbarHostState  = remember { SnackbarHostState() }

    val showWarning : (Int) -> Unit = { it ->
        coroutineScope.launch {
            Log.d("VEDROID", "showWarning: id = $it, text ='${context.getString( it)}'")
            snackbarHostState.showSnackbar(message = context.getString( it))
        }
    }
    val loading : (Boolean) -> Unit = { it -> isLoading = it}
    val loadJoke : (Joke) -> Unit = { it -> jokes.add(it)}
    val download : () -> Unit = {
        downloadJokes(coroutineScope, loading, loadJoke, showWarning)
    }

    LaunchedEffect(Unit) {
        download()
    }
    FragmentFinalScreen(jokes, isLoading, download, snackbarHostState)
}

@Composable
fun FragmentFinalScreen(
        jokes: MutableSet<Joke>,
        isLoading: Boolean,
        download : () -> Unit = { Log.d("VEDROID", "FragmentFinalScreen: download...") },
        snackbarHostState: SnackbarHostState = SnackbarHostState(),
){

    Scaffold(
        snackbarHost = { SnackbarHost ( hostState = snackbarHostState ) },
        containerColor = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        PullToRefreshBox(
            state = rememberPullToRefreshState(),
            isRefreshing = isLoading,
            onRefresh = download,
            modifier = Modifier.padding(innerPadding)
                .background(MaterialTheme.colorScheme.onTertiary)
        ) {
            Column {

                LoadingIndicator(isLoading = isLoading)
                if (jokes.isEmpty()) {
                    WarningMessage(isLoading)
                }
                JokeItemsList(jokes)

            }
        }
    }


}

@Composable
fun WarningMessage (isLoading: Boolean){
    Text(
        text = stringResource( id = if(!isLoading) R.string.warning_text_no_data else R.string.warning_text_loading),

        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun LoadingIndicator(isLoading: Boolean) {
    if (!isLoading) return
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    )
//    CircularProgressIndicator(
//        modifier = Modifier.width(64.dp),
//        color = MaterialTheme.colorScheme.secondary,
//        trackColor = MaterialTheme.colorScheme.surfaceVariant,
//    )
}

@Composable
private fun JokeItemsList (jokes: MutableSet<Joke>){
    LazyColumn(Modifier.fillMaxSize()) {
        jokes.forEach {
            item { ItemJoke(
                isSingle = it.isSingle,
                setup = it.setup,
                delivery = it.delivery)
            }
        }
    }
}

private fun downloadJokes (
    coroutineScope : CoroutineScope,
    loading : (Boolean) -> Unit = { it -> Log.d("VEDROID", "downloadJokes: is show loading = $it") },
    loadJoke : (Joke) -> Unit = { it -> Log.d("VEDROID", "downloadJokes: load new joke $it") },
    showWarning : (Int) -> Unit = { it -> Log.d("VEDROID", "downloadJokes: warning id = $it") }) {

    val isAsynchronous : Boolean = false

    // show loadin indicator
    loading(true)

    // try load data
    if (isAsynchronous) {
        // asynchronous from Retrofit with enqueue
        RetrofitClient.apiService.callJokes().enqueue(object : Callback<JokesRequest.ApiJokesList> {
            override fun onFailure(call: Call<JokesRequest.ApiJokesList>, t: Throwable) {
                t.message?.let { Log.d("VEDROID", "downloadJokes: onFailure, exception = $it") }
                showWarning(R.string.warning_text_no_internet)

                // hide loadin indicator
                loading(false)
            }

            override fun onResponse(
                call: Call<JokesRequest.ApiJokesList>,
                response: Response<JokesRequest.ApiJokesList>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()?.jokes as MutableList<JokesRequest>
                    list.forEach {
                        Log.d("VEDROID", "downloadJokes: isSuccessful $it")
                        loadJoke(it.parseToJoke())
                    }
                } else {
                    Log.d("VEDROID", "downloadJokes: notSuccessful, code = ${response.code()}")
                }

                // hide loadin indicator
                loading(false)
            }
        })
    } else {
        // synchronous in a background thread
        coroutineScope.launch{
            runCatching {
                RetrofitClient.apiService.getJokes()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.jokes?.forEach {
                        Log.d("VEDROID", "downloadJokes: isSuccessful $it")
                        loadJoke(it.parseToJoke())
                    }
                } else{
                    Log.d("VEDROID", "downloadJokes: notSuccessful, code = ${response.code()}")
                }
            }.onFailure { throwable ->
                Log.d("VEDROID", "downloadJokes: onFailure, exception = $throwable")
                showWarning(R.string.warning_text_no_internet)
            }.also {
                // hide loadin indicator
                loading(false)
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
    val jokes = mutableSetOf<Joke>()
    mock.forEach { jokes.add(Joke(jokes.size, true, it)) }

    MobileAndroidAppComposeTheme (dynamicColor = false) {
        FragmentFinalScreen(jokes = jokes.toMutableSet(), isLoading =  true)
//        FragmentFinalScreen(jokes = jokes.toMutableSet(), isLoading = false)
//        FragmentFinalScreen(jokes = mutableSetOf<JokesRequest>(), isLoading = true)
//        FragmentFinalScreen(jokes = mutableSetOf<JokesRequest>(), isLoading = false)
    }
}