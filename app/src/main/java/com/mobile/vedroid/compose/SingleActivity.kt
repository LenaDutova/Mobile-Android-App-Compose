package com.mobile.vedroid.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mobile.vedroid.compose.ui.compose.FragmentFinal
import com.mobile.vedroid.compose.ui.compose.FragmentReturning
import com.mobile.vedroid.compose.ui.compose.FragmentSettings
import com.mobile.vedroid.compose.ui.compose.FragmentStart
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme

class SingleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MobileAndroidAppComposeTheme {
                val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                val backStack = rememberNavBackStack(SingleActivityRoutes.Start())

                Box( Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomPadding, top = topPadding) ) {

                    LifecycleEventEffect(Lifecycle.Event.ON_START) {
                        Log.d("TAG", "Screen: ON_START")
                    }

                    NavDisplay(
                        backStack = backStack,
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            } else {
                                finish()
                            }
                        },
                        entryProvider = entryProvider {
                            entry<SingleActivityRoutes.Start> { route ->
                                FragmentStart(
                                    name = route.name,
                                    sex = route.sex,
                                    returningInClick = {
                                        backStack.add(SingleActivityRoutes.Returning)
                                    },
                                    finalClick = {
                                        backStack.add(SingleActivityRoutes.Final)
                                    },
                                    settingsClick = {
                                        backStack.add(SingleActivityRoutes.Settings)
                                    },
                                )
                            }

                            entry<SingleActivityRoutes.Returning> {
                                FragmentReturning(
                                    returningInClick = { name, sex ->
                                        backStack.add(SingleActivityRoutes.Start(name, sex))
                                    },
                                )
                            }

                            entry<SingleActivityRoutes.Final> {
                                FragmentFinal()
                            }

                            entry<SingleActivityRoutes.Settings> {
                                FragmentSettings(
                                    closeClick = { backStack.removeLastOrNull() },
                                    logOutClick = {
                                        backStack.clear()
                                        backStack.add(SingleActivityRoutes.Start())
                                    },
                                )
                            }
                        },
                    )

                }
            }
        }
    }
}