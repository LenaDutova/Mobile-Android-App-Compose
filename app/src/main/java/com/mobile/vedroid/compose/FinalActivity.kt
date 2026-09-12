package com.mobile.vedroid.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobile.vedroid.compose.ui.compose.ItemJoke
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme

class FinalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // https://metanit.com/kotlin/jetpack/2.14.php
        setContent {
            MobileAndroidAppComposeTheme {
                val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                Column( Modifier.fillMaxSize().padding(bottom = bottomPadding, top = topPadding) ) {
                    FragmentFinal()
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
    val mock = listOf("Java", "Kotlin", "HTML", "CSS", "JavaScript", "SQL", "Python",  "C++", "Assembler", "Pascal", "Prolog", "Lisp", "C#")

    MobileAndroidAppComposeTheme (dynamicColor = false) {
        FragmentFinal(mock)
//            FragmentFinal(emptyList())
//            FragmentFinal()
    }
}

@Composable
private fun FragmentFinal(jokes: List<String>? = null){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
    ) {
        if (jokes == null || jokes?.isEmpty() == true) {
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
                jokes?.forEach {
                    item { ItemJoke(setup = it) }
                }
            }
        }
    }
}