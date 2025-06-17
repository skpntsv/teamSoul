package ru.nsu.teamsoul.ui.view.screen.splash

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nsu.teamsoul.data.repository.AuthRepository

sealed class SplashState {
    data object Loading : SplashState()
    data object NavigateToLogin : SplashState()
    data object NavigateToMain : SplashState()
}

class SplashViewModel(private val authRepository: AuthRepository) : ScreenModel {
    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state = _state.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        screenModelScope.launch {
            if (authRepository.isLoggedIn()) {
                _state.value = SplashState.NavigateToMain
            } else {
                _state.value = SplashState.NavigateToLogin
            }
        }
    }
}