package com.mobile.vedroid.compose.ui.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mobile.vedroid.compose.R
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
public fun FragmentStart (
    name: String? = null,
    sex : Boolean? = false,

    returningInClick: () -> Unit = { Log.d("TAG_FragmentStart", "click to returning screen")},
    finalClick: () -> Unit = { Log.d("TAG_FragmentStart", "click to final screen")},
    settingsClick:() -> Unit = { Log.d("TAG_FragmentStart", "click to settings screen")}
){
    Scaffold(
        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    alignment = Alignment.End),
            ) {
                OutlinedButton (
                    onClick = returningInClick as () -> Unit,
                    border = BorderStroke(
                        ButtonDefaults.outlinedButtonBorder(true).width,
                        MaterialTheme.colorScheme.primary
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon (
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.btn_introduce_yourself)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.btn_introduce_yourself))
                }

                Button (
                    onClick = finalClick
                ) {
                    Text(stringResource(R.string.btn_next))
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val (fab, greeting, screen) = createRefs()
            Image(
                painter = painterResource(R.drawable.jetpack_compose_big),
                contentDescription = "Jetpack Compose",

                modifier = Modifier
                    .size(256.dp)
                    .constrainAs(screen){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            FloatingActionButton(
                onClick = settingsClick,
                modifier = Modifier
                    .constrainAs(fab){
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            ){
                Icon(Icons.Filled.Settings, "settings")
            }

            Text (
                text = buildString {
                    if (name.isNullOrBlank()) append(stringResource(R.string.text_greeting_anonymous))
                    else {
                        append(stringResource(R.string.text_greeting))
                        append(" ")
                        if (sex!!){
                            append(stringResource(R.string.text_mr))
                        } else {
                            append(stringResource(R.string.text_mrs))
                        }
                        append(" ")
                        append(name)
                    }
                },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier =
                    Modifier.constrainAs(greeting){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
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
private fun PreviewFragmentStart(){
    MobileAndroidAppComposeTheme (dynamicColor = false) {
        FragmentStart()
//        FragmentStart(Account("Anna", false))
//        FragmentStart(Account("Bob", true))
    }
}