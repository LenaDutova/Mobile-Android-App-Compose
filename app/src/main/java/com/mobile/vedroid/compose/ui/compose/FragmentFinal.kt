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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobile.vedroid.compose.MobileApplication
import com.mobile.vedroid.compose.R
import com.mobile.vedroid.compose.model.Joke
import com.mobile.vedroid.compose.extensions.parseToJoke
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
    val jokes = rememberSaveable { mutableStateListOf<Joke>() }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope  = rememberCoroutineScope()
    val snackbarHostState  = remember { SnackbarHostState() }

    val showWarning : (Int) -> Unit = { it ->
        coroutineScope.launch {
            Log.d("VEDRO_ID", "showWarning: id = $it, text ='${context.getString( it)}'")
            snackbarHostState.showSnackbar(message = context.getString( it))
        }
    }
    val loading : (Boolean) -> Unit = { it -> isLoading = it}
    val loadJoke : (Joke) -> Boolean = { it ->
        if (!jokes.contains(it)) {
            jokes.add(0, it)
            true
        } else {
            Log.d("VEDRO_ID", "loadJoke: duplicate")
            if (jokes.get (jokes.indexOf (it).isUpdated (it)) {
                val position = jokes.indexOf (it)
                jokes.removeAt (position)
                jokes.add (position, it)
                true
            } else false
        }
    }
    val download : () -> Unit = {
        downloadJokes(coroutineScope, loading, loadJoke, showWarning)
    }

    LaunchedEffect(Unit) {
        MobileApplication.jokesRepository().load()
            .collect { items ->
                if (items.isEmpty()) download()
                else items.forEach { it ->
                    loadJoke(it)
                }
            }
    }
    FragmentFinalScreen(jokes, isLoading, download, snackbarHostState)
}

@Composable
fun FragmentFinalScreen(
        jokes: MutableList<Joke>,
        isLoading: Boolean,
        download : () -> Unit = { Log.d("VEDRO_ID", "FragmentFinalScreen: download...") },
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
}

@Composable
private fun JokeItemsList (jokes: MutableList<Joke>){
//    val listState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()

    LazyColumn( /*state = listState,*/
        modifier =  Modifier.fillMaxSize()) {
        jokes.forEach {
            item { ItemJoke(
                isSingle = it.isSingle,
                setup = it.setup,
                delivery = it.delivery)
            }
        }
//        if (!jokes.isEmpty()) {
//            coroutineScope.launch { listState.scrollToItem(jokes.size - 1) }
//        }
    }
}

private fun downloadJokes (
    coroutineScope : CoroutineScope,
    loading : (Boolean) -> Unit = { it -> Log.d("VEDRO_ID", "downloadJokes: is show loading = $it") },
    loadJoke : (Joke) -> Boolean = { it ->
        Log.d("VEDRO_ID", "downloadJokes: load new joke $it")
        false },
    showWarning : (Int) -> Unit = { it -> Log.d("VEDRO_ID", "downloadJokes: warning id = $it") },

    isAsynchronous : Boolean = false) {

    // if no internet - only show warning
    if (!RetrofitClient.isOnline(MobileApplication.context())) {
        showWarning(R.string.warning_text_no_internet)
        return
    }

    // show loadin indicator
    loading(true)

    // try load data
    if (isAsynchronous) {
        // asynchronous from Retrofit with enqueue
        RetrofitClient.apiService.callJokes().enqueue(object : Callback<JokesRequest.ApiJokesList> {
            override fun onFailure(call: Call<JokesRequest.ApiJokesList>, t: Throwable) {
                t.message?.let { Log.e("VEDRO_ID", "downloadJokes: onFailure, exception = $it", t) }
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
                        Log.d("VEDRO_ID", "downloadJokes: isSuccessful $it")
                        if (loadJoke(it.parseToJoke()))
                            coroutineScope.launch{
                                MobileApplication.jokesRepository().save(it.parseToJoke())
                            }
                    }
                } else {
                    Log.d("VEDRO_ID", "downloadJokes: notSuccessful, code = ${response.code()}")
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
                        Log.d("VEDRO_ID", "downloadJokes: isSuccessful $it")
                        if (loadJoke(it.parseToJoke()))
                            MobileApplication.jokesRepository().save(it.parseToJoke())
                    }
                } else{
                    Log.d("VEDRO_ID", "downloadJokes: notSuccessful, code = ${response.code()}")
                }
            }.onFailure { throwable ->
                Log.e("VEDRO_ID", "downloadJokes: onFailure, exception = $throwable", throwable)
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
    val jokes = mutableListOf<Joke>()
    mock.forEach { jokes.add(Joke(jokes.size, true, it)) }

    MobileAndroidAppComposeTheme (dynamicColor = false) {
        FragmentFinalScreen(jokes = jokes, isLoading =  true)
//        FragmentFinalScreen(jokes = jokes.toMutableSet(), isLoading = false)
//        FragmentFinalScreen(jokes = mutableSetOf<JokesRequest>(), isLoading = true)
//        FragmentFinalScreen(jokes = mutableSetOf<JokesRequest>(), isLoading = false)
    }
}