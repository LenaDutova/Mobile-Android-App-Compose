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
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mobile.vedroid.compose.ui.compose.FragmentFinal
import com.mobile.vedroid.compose.ui.compose.FragmentReturning
import com.mobile.vedroid.compose.ui.compose.FragmentSettings
import com.mobile.vedroid.compose.ui.compose.FragmentStart
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme
import kotlinx.serialization.Serializable

class SingleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAndroidAppComposeTheme {
                val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                val navController = rememberNavController()

                Box( Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomPadding, top = topPadding) ) {

                    LifecycleEventEffect(Lifecycle.Event.ON_START) {
                        Log.d("TAG", "Screen: ON_START")
                    }
                    NavHost ( navController, startDestination = SingleActivityRoutes.Start() ) {
                        composable<SingleActivityRoutes.Start> {
                            backStackEntry -> val account: SingleActivityRoutes.Start = backStackEntry.toRoute()
                            FragmentStart (
                                account.name,
                                account.sex,
                                returningInClick = { navController.navigate(route = SingleActivityRoutes.Returning) },
                                finalClick = { navController.navigate(route = SingleActivityRoutes.Final) },
                                settingsClick = { navController.navigate(route = SingleActivityRoutes.Settings) }
                            )
                        }
                        composable <SingleActivityRoutes.Returning> {
                            FragmentReturning (
                                returningInClick = {
                                    name, sex -> navController.navigate(route = SingleActivityRoutes.Start(name, sex))
                                }
                            )
                        }
                        composable <SingleActivityRoutes.Final> {
                            FragmentFinal ()
                        }
                        composable <SingleActivityRoutes.Settings> {
                            FragmentSettings (
                                closeClick = { navController.popBackStack(); },
                                logOutClick = {
                                    // TODO // LogOut & Return to Start
                                    navController.navigate(route = SingleActivityRoutes.Start())
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}