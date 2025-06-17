package ru.nsu.teamsoul.ui.view.screen.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nsu.teamsoul.data.repository.AuthRepository

data class LoginUiState(
    val identifier: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class LoginEvent {
    data object NavigateToMain : LoginEvent()
}

class LoginViewModel(private val authRepository: AuthRepository) : ScreenModel {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    fun onIdentifierChange(value: String) {
        _uiState.update { it.copy(identifier = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun login() {
        if (_uiState.value.isLoading) return

        if (_uiState.value.identifier.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.update { it.copy(error = "Логин и пароль не могут быть пустыми") }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val loginSuccessful = authRepository.login(
                identifier = _uiState.value.identifier,
                password = _uiState.value.password
            )
            if (loginSuccessful) {
                _events.emit(LoginEvent.NavigateToMain)
            } else {
                _uiState.update { it.copy(error = "Неверный логин или пароль", isLoading = false) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}