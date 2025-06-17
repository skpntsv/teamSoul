package ru.nsu.teamsoul

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinApplication
import ru.nsu.teamsoul.di.commonModule
import ru.nsu.teamsoul.ui.theme.AppTheme
import ru.nsu.teamsoul.ui.view.screen.splash.SplashScreen

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(commonModule)
        }
    ) {
        AppTheme {
            Navigator(SplashScreen())
        }
    }
}