package ru.nsu.teamsoul.ui.view.screen.gameselection

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nsu.teamsoul.data.remote.dto.GamePluginsResponse
import ru.nsu.teamsoul.data.repository.GameRepository

data class GameSelectionUiState(
    val isLoading: Boolean = true,
    val games: List<GamePluginsResponse> = emptyList(),
    val error: String? = null
)

sealed class GameSelectionNavigationEvent {
    data class NavigateToGameWebView(val url: String) : GameSelectionNavigationEvent()
}

class GameSelectionViewModel(
    private val gameRepository: GameRepository
) : ScreenModel {
    private val _uiState = MutableStateFlow(GameSelectionUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<GameSelectionNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        loadGames()
    }

    fun startGame(gameId: Long, roomId: Long) {
        if (_uiState.value.isLoading) return

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            gameRepository.createGameInRoom(gameId, roomId)
                .onSuccess { url ->
                    delay(2000L)
                    _navigationEvents.emit(GameSelectionNavigationEvent.NavigateToGameWebView(url))
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(error = exception.message) }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadGames() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            gameRepository.getGamesList()
                .onSuccess { games ->
                    _uiState.update { it.copy(games = games, isLoading = false) }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(error = exception.message, isLoading = false) }
                }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }
}