package ru.nsu.teamsoul

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
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
            Navigator(screen = SplashScreen) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}