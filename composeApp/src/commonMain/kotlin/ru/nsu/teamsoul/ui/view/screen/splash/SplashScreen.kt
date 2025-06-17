package ru.nsu.teamsoul.ui.view.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject
import ru.nsu.teamsoul.ui.view.screen.login.LoginScreen
import ru.nsu.teamsoul.ui.view.screen.main.MainScreen

object SplashScreen : Screen {
    @Composable
    override fun Content() {
        SplashScreen()
    }
}

@Composable
fun SplashScreen(viewModel: SplashViewModel = koinInject()) {
    val navigator = LocalNavigator.currentOrThrow
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value) {
        when (state.value) {
            SplashState.NavigateToLogin -> navigator.replaceAll(LoginScreen)
            SplashState.NavigateToMain -> navigator.replaceAll(MainScreen)
            SplashState.Loading -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("TeamSoul")
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}