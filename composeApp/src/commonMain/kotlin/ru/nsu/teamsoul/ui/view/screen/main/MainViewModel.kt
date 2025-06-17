package ru.nsu.teamsoul.ui.view.screen.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nsu.teamsoul.data.repository.GameRepository

data class MainUiState(
    val isLoading: Boolean = false,
    val showJoinGameDialog: Boolean = false,
    val error: String? = null
)

sealed class MainNavigationEvent {
    data class NavigateToGameSelection(val roomId: Int) : MainNavigationEvent()
}

class MainViewModel(
    private val gameRepository: GameRepository
) : ScreenModel {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<MainNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onJoinGameClicked() {
        _uiState.update { it.copy(showJoinGameDialog = true) }
    }

    fun onJoinGameDialogDismiss() {
        _uiState.update { it.copy(showJoinGameDialog = false) }
    }

    fun onCreateRoomClicked() {
        if (_uiState.value.isLoading) return

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            gameRepository.createRoom()
                .onSuccess { roomId ->
                    _navigationEvents.emit(MainNavigationEvent.NavigateToGameSelection(roomId))
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(error = exception.message ?: "Неизвестная ошибка") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }
}