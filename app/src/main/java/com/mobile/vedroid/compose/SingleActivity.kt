package com.mobile.vedroid.compose

import android.os.Bundle
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
                    NavHost ( navController, startDestination = Start() ) {
                        composable<Start> {
                            backStackEntry -> val account: Start = backStackEntry.toRoute()
                            FragmentStart (
                                account.name,
                                account.sex,
                                returningInClick = { navController.navigate(route = Returning) },
                                finalClick = { navController.navigate(route = Final) },
                                settingsClick = { navController.navigate(route = Settings) }
                            )
                        }
                        composable <Returning> {
                            FragmentReturning (
                                returningInClick = {
                                    name, sex -> navController.navigate(route = Start(name, sex))
                                }
                            )
                        }
                        composable <Final> {
                            FragmentFinal ()
                        }
                        composable <Settings> {
                            FragmentSettings (
                                closeClick = { navController.popBackStack(); },
                                logOutClick = {
                                    // TODO // LogOut & Return to Start
                                    navController.navigate(route = Start())
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Serializable
data class Start (val name: String? = null, val sex: Boolean? = false)

@Serializable
object Returning

@Serializable
object Settings

@Serializable
object Final