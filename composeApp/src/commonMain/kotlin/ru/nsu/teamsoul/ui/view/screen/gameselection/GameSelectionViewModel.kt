package ru.nsu.teamsoul.ui.view.screen.gameselection

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
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

class GameSelectionViewModel(
    private val gameRepository: GameRepository
) : ScreenModel {
    private val _uiState = MutableStateFlow(GameSelectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
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