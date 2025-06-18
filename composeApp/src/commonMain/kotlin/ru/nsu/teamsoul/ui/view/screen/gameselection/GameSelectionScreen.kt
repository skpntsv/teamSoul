package ru.nsu.teamsoul.ui.view.screen.gameselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { AppHeader() },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.popAll() },
                        modifier = Modifier.fillMaxWidth(0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.button_cancel)
                        )
                    }
                },
                actions = { Spacer(Modifier.fillMaxWidth(0.1f)) }
            )
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
        ) {
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
    Box(
        modifier = Modifier
            .width(270.dp)
            .height(280.dp)
            .clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier
                .width(220.dp)
                .height(240.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color(0xFF4BEDFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                game.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Image(
            painter = painterResource(Res.drawable.img_card_game_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 20.dp, y = 40.dp)
                .size(200.dp)
        )
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