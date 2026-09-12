package com.mobile.vedroid.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.twotone.LightMode
import androidx.compose.material.icons.twotone.NightsStay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // https://metanit.com/kotlin/jetpack/2.14.php
        setContent {
            MobileAndroidAppComposeTheme {
                val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                Column( Modifier.fillMaxSize().padding(bottom = bottomPadding, top = topPadding) ) {
                    FragmentSettings(
                        logOutClick = {
                            // TODO // LogOut & Return to Start
                            finish()
                        },
                        closeClick = { finish() }
                    )
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
private fun PreviewFragmentSettings(){
    MobileAndroidAppComposeTheme (dynamicColor = false) {
        FragmentSettings()
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
private fun FragmentSettings(
    logOutClick:() -> Unit = { Log.d("FragmentSettings", "click to log out and start screen") },
    closeClick:() -> Unit = { Log.d("FragmentSettings", "click to start screen") }
){
    val context = LocalContext.current
    val options = listOf(
        context.getString(R.string.toggle_mode_dark),
        context.getString(R.string.toggle_mode_system),
        context.getString(R.string.toggle_mode_light)
    )

    var language by rememberSaveable { mutableStateOf(false) }
    var theme by rememberSaveable { mutableIntStateOf (1) }

    Scaffold(
        bottomBar = {
            Row (
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    alignment = Alignment.End),
            ) {
                OutlinedButton (
                    onClick = logOutClick,
                    border = BorderStroke(
                        ButtonDefaults.outlinedButtonBorder(true).width,
                        MaterialTheme.colorScheme.primary
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon (
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = stringResource(R.string.btn_close)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.btn_log_out))
                }

                Button (
                    onClick = closeClick
                ) {
                    Text(stringResource(R.string.btn_close))
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
            val(checkbox, buttonGroup) = createRefs()

            SingleChoiceSegmentedButtonRow(modifier = Modifier
                .constrainAs(buttonGroup) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){
                options.forEachIndexed {index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        contentPadding = SegmentedButtonDefaults.ContentPadding,
                        icon = {
                            // NightsStay
                            when (index){
                                0 -> Icon (
                                    imageVector = Icons.TwoTone.NightsStay,
                                    contentDescription = label
                                )
                                2 -> Icon (
                                    imageVector = Icons.TwoTone.LightMode,
                                    contentDescription = label
                                )
                            }
                        },
                        onClick = {
                            theme = index
                            // TODO() // save data
                        },
                        selected = index == theme,
                    ) {
                        Text(label)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(checkbox){
                    top.linkTo(buttonGroup.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                Checkbox(
                    checked = language,
                    onCheckedChange = {
                        language = it
                        // TODO() // save data
                    }
                )
                Text(text = stringResource(R.string.checkbox_language_ru))
            }

        }
    }
}