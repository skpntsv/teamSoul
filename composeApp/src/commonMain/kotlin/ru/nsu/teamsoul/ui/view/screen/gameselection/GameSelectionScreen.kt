package ru.nsu.teamsoul.ui.view.screen.gameselection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import ru.nsu.teamsoul.data.remote.dto.GamePluginsResponse
import ru.nsu.teamsoul.ui.components.AppHeader
import ru.nsu.teamsoul.ui.view.screen.webview.GameWebViewScreen
import teamsoul.composeapp.generated.resources.Res
import teamsoul.composeapp.generated.resources.button_cancel
import teamsoul.composeapp.generated.resources.game_description_dialog_button_start
import teamsoul.composeapp.generated.resources.game_selection_choose_game
import teamsoul.composeapp.generated.resources.game_selection_snackbar_room_created
import teamsoul.composeapp.generated.resources.game_selection_title
import teamsoul.composeapp.generated.resources.img_card_game_logo

data class GameSelectionScreen(val roomId: Long) : Screen {
    @Composable
    override fun Content() {
        GameSelectionRoute(roomId = roomId)
    }
}

@Composable
fun GameSelectionRoute(
    roomId: Long,
    viewModel: GameSelectionViewModel = koinInject()
) {
    val navigator = LocalNavigator.currentOrThrow
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val gameSnackBarTittle = stringResource(Res.string.game_selection_snackbar_room_created)

    LaunchedEffect(Unit) {
        snackbarHostState.showSnackbar(gameSnackBarTittle)
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(uiState.error!!)
            viewModel.onErrorShown()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is GameSelectionNavigationEvent.NavigateToGameWebView -> {
                    navigator.push(GameWebViewScreen(event.url))
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
        ) {
            AppHeader()
            Text(
                text = stringResource(Res.string.game_selection_title, roomId),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(Res.string.game_selection_choose_game),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                GamesList(
                    games = uiState.games,
                    roomId = roomId,
                    onStartGame = viewModel::startGame
                )
            }
        }
    }
}

@Composable
private fun GamesList(
    games: List<GamePluginsResponse>,
    roomId: Long,
    onStartGame: (gameId: Long, roomId: Long) -> Unit
) {
    var showDialogForGame by remember { mutableStateOf<GamePluginsResponse?>(null) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(games) { game ->
            GameCard(game = game, onClick = { showDialogForGame = game })
        }
    }

    showDialogForGame?.let { game ->
        GameDescriptionDialog(
            game = game,
            roomId = roomId,
            onDismiss = { showDialogForGame = null },
            onStartGame = onStartGame
        )
    }
}

@Composable
private fun GameCard(game: GamePluginsResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = game.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(8.dp))
            game.description?.let {
                Text(
                    text = it,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                painterResource(Res.drawable.img_card_game_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(1f)
            )
        }
    }
}

@Composable
private fun GameDescriptionDialog(
    game: GamePluginsResponse,
    roomId: Long,
    onDismiss: () -> Unit,
    onStartGame: (gameId: Long, roomId: Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = game.name,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            game.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        confirmButton = {
            Button(onClick = { onStartGame(game.id, roomId) }) {
                Text(
                    text = stringResource(Res.string.game_description_dialog_button_start),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(Res.string.button_cancel),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}