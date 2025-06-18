package ru.nsu.teamsoul.ui.view.screen.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.nsu.teamsoul.data.repository.AuthRepository

data class MainHeaderUiState(
    val username: String? = null,
    val showLogoutDialog: Boolean = false
)

sealed class MainHeaderNavigationEvent {
    data object NavigateToLogin : MainHeaderNavigationEvent()
}

class MainHeaderViewModel(
    private val authRepository: AuthRepository
) : ScreenModel {

    private val _uiState = MutableStateFlow(MainHeaderUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<MainHeaderNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        loadUserDetails()
    }

    private fun loadUserDetails() {
        screenModelScope.launch {
            val userDetails = authRepository.getUserDetails()
            _uiState.update { it.copy(username = userDetails?.login) }
        }
    }

    fun onLogoutClicked() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onLogoutDialogDismiss() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onLogoutConfirm() {
        screenModelScope.launch {
            authRepository.logout()
            _navigationEvents.emit(MainHeaderNavigationEvent.NavigateToLogin)
        }
    }
}