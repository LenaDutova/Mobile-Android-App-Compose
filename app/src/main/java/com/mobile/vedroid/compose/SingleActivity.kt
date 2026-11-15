package com.mobile.vedroid.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mobile.vedroid.compose.storage.AccountRepository
import com.mobile.vedroid.compose.storage.SettingsRepository
import com.mobile.vedroid.compose.ui.SingleActivityRoutes
import com.mobile.vedroid.compose.ui.compose.FragmentFinal
import com.mobile.vedroid.compose.ui.compose.FragmentReturning
import com.mobile.vedroid.compose.ui.compose.FragmentSettings
import com.mobile.vedroid.compose.ui.compose.FragmentStart
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme
import com.mobile.vedroid.compose.ui.theme.AppMode
import kotlinx.coroutines.launch

class SingleActivity : ComponentActivity() {

//    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val vedroidMode : AppMode by SettingsRepository.loadThemeMode().collectAsStateWithLifecycle(initialValue = AppMode.SYSTEM)
            val vedroidOnlyRuLanguage by SettingsRepository.loadOnlyRuLanguage().collectAsStateWithLifecycle(initialValue = false)

            MobileAndroidAppComposeTheme (vedroidOnlyRuLanguage, themeMode = vedroidMode, dynamicColor = false) {
                val backStack = rememberNavBackStack(
                    if (AccountRepository.hasSavedAccount()) SingleActivityRoutes.Start(AccountRepository.loadAccount())
                    else SingleActivityRoutes.Start())

//                val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
//                val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
//                  // without Scaffold need add Window paddings
//                Box( Modifier.fillMaxSize()
//                    .padding(bottom = bottomPadding, top = topPadding) ) {

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
                                user = route.user,

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
                                returningInClick = { user ->
                                    // Save new account data
                                    AccountRepository.saveAccount(user)
                                    backStack.add(SingleActivityRoutes.Start( user ))
                                }
                            )
                        }

                        entry<SingleActivityRoutes.Final> {
                            FragmentFinal()
                        }

                        entry<SingleActivityRoutes.Settings> {
                            FragmentSettings (
                                mode = vedroidMode,
                                isOnlyRu = vedroidOnlyRuLanguage,

                                closeClick = { backStack.removeLastOrNull() },
                                logOutClick = {
                                    // Clear saved account and return
                                    AccountRepository.clearAccount()

                                    backStack.clear()
                                    backStack.add(SingleActivityRoutes.Start())
                                },

                                modeChange = { mode -> if (vedroidMode != mode) {
                                    // Save mode data
                                    lifecycleScope.launch{
                                        SettingsRepository.saveThemeMode(mode)
                                    }
                                } },
                                languageChange = { isOnlyRu -> if (isOnlyRu != vedroidOnlyRuLanguage) {
                                    // save data about RU language needed
                                        lifecycleScope.launch {
                                            SettingsRepository.saveOnlyRuLanguage(isOnlyRu)
                                        }
                                    }
                                }
                            )
                        }
                    },
                )

//                LifecycleEventEffect(Lifecycle.Event.ON_START) {
//                    Log.d("VEDRO_ID", "SingleActivity screen: ON_START")
//                }

//                }

            }
        }
    }
}