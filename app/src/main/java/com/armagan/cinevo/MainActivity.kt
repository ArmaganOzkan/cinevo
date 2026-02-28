package com.armagan.cinevo

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.armagan.cinevo.data.local.RememberMeRepository
import com.armagan.cinevo.ui.theme.DiziboxTheme
import  com.armagan.cinevo.navigation.*
import com.armagan.cinevo.ui.customcomposables.NoInternetScreen
import com.armagan.cinevo.util.LanguagePreference
import com.armagan.cinevo.util.LocalAppLanguage
import com.armagan.cinevo.util.ThemePreference
import com.armagan.cinevo.util.network.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.armagan.cinevo.ui.customcomposables.NoInternetScreen



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rememberRepo = RememberMeRepository(this)
        var startDestination = Screen.LoginScreen.route

        val themePref = ThemePreference(this)
        val isDarkThemeEnabled = themePref.isDarkTheme()

        val languagePref = LanguagePreference(this)
        val savedLanguage = languagePref.getLanguage()

        updateLocale(savedLanguage)

        runBlocking {
            val (remember, email) = rememberRepo.rememberMeFlow.first()
            if (remember && !email.isNullOrBlank()) {
                startDestination = Screen.MainScreen.route
            }
        }

        // 🔥 NETWORK OBSERVER
        val connectivityObserver = NetworkConnectivityObserver(this)

        setContent {

            val isConnected by connectivityObserver
                .isConnected
                .collectAsState(initial = true)

            val themePref = remember { ThemePreference(this) }
            val languagePref = remember { LanguagePreference(this) }
            val selectedLanguage = remember { mutableStateOf(languagePref.getLanguage()) }

            CompositionLocalProvider(LocalAppLanguage provides selectedLanguage.value) {

                DiziboxTheme(darkTheme = themePref.isDarkTheme()) {

                    Surface(modifier = Modifier.fillMaxSize()) {

                        if (!isConnected) {

                            // 🚨 INTERNET YOKSA
                            NoInternetScreen(
                                onRetryClick = {
                                    // Flow zaten otomatik tetikleniyor
                                    // ekstra bir şey yapmaya gerek yok
                                }
                            )

                        } else {

                            // ✅ INTERNET VARSA NORMAL NAVIGATION
                            Navigation(
                                startDestination = startDestination,
                                languageState = selectedLanguage,
                                languagePref = languagePref
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateLocale(languageCode: String) {
        val locale = java.util.Locale(languageCode)
        java.util.Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}


@HiltAndroidApp
class MyApp : Application()

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DiziboxTheme {
//        Navigation(Screen.LoginScreen.route)
//    }
//}
