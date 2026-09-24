package com.mobile.vedroid.compose.ui.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mobile.vedroid.compose.R
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
public fun FragmentReturning(
    returningInClick: (String, Boolean) -> Unit = { name, sex -> Log.d("TAG_FragmentStart", "click from returning screen")}
){
    val context = LocalContext.current
    val options = listOf(
        context.getString(R.string.toggle_sex_man),
        context.getString(R.string.toggle_sex_not_defined),
        context.getString(R.string.toggle_sex_woman)
    )

    var name by rememberSaveable { mutableStateOf("") }
    var sex by rememberSaveable { mutableIntStateOf (1) }
    val snackbarHostState  = remember { SnackbarHostState() }
    val coroutineScope  = rememberCoroutineScope()

    val isNameError = name.isEmpty()
    val isGenderError = sex == 1
    val logInClick:() -> Unit = {
        if (isNameError || isGenderError){
            coroutineScope.launch {
                val message = buildString {
                    append(context.getString(R.string.text_please))
                    if (isNameError) append(context.getString(R.string.text_no_name))
                    if (isGenderError) append(context.getString(R.string.text_no_gender))
                }
                snackbarHostState.showSnackbar(message)
            }
        } else {
//            TODO() // Save new account data & Return to Start
            returningInClick (name, sex == 0)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost ( hostState = snackbarHostState ) },
        modifier = Modifier.fillMaxSize()
    ){ innerPadding ->
        ConstraintLayout (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        )  {
            val(textField, button, buttonGroup) = createRefs()

            OutlinedTextField (
                value = name,
                onValueChange = { name = it },
                label = {
                    Text( "${stringResource(R.string.text_please)}${stringResource(R.string.text_no_name)}")
                },
                placeholder = {
                    Text(stringResource(R.string.et_enter_name))
                },
                isError = isNameError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(textField) {
                        bottom.linkTo(buttonGroup.top, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

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
                        icon = {},
                        onClick = { sex = index },
                        selected = index == sex,
                    ) {
                        Text(label)
                    }
                }
            }

            Button (
                onClick = logInClick,
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(buttonGroup.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(stringResource(R.string.btn_register))
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
private fun PreviewFragmentReturning(){
    MobileAndroidAppComposeTheme (dynamicColor = false) {
        FragmentReturning()
    }
}