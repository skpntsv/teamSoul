package ru.nsu.teamsoul.navigation

import cafe.adriel.voyager.core.screen.Screen
import ru.nsu.teamsoul.ui.view.screen.login.LoginScreen
import ru.nsu.teamsoul.ui.view.screen.main.MainScreen
import ru.nsu.teamsoul.ui.view.screen.splash.SplashScreen

sealed class AppScreen {
    data object Splash : Screen by SplashScreen()
    data object Login : Screen by LoginScreen()
    data object Main : Screen by MainScreen()
}