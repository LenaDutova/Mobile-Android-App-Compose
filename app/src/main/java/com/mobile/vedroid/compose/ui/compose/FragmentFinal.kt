package com.mobile.vedroid.compose.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobile.vedroid.compose.R
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme

@Composable
public fun FragmentFinal(jokes: List<String>? = null){
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