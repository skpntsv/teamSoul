package ru.nsu.teamsoul.ui.view.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import ru.nsu.teamsoul.ui.components.AppHeader
import ru.nsu.teamsoul.ui.theme.TeamSoulBlue
import ru.nsu.teamsoul.ui.theme.TextPrimary
import ru.nsu.teamsoul.ui.theme.TextSecondary
import ru.nsu.teamsoul.ui.view.screen.gameselection.GameSelectionScreen
import ru.nsu.teamsoul.ui.view.screen.webview.GameWebViewScreen
import teamsoul.composeapp.generated.resources.Res
import teamsoul.composeapp.generated.resources.button_cancel
import teamsoul.composeapp.generated.resources.img_team_puzzle
import teamsoul.composeapp.generated.resources.join_dialog_button_join
import teamsoul.composeapp.generated.resources.join_dialog_description
import teamsoul.composeapp.generated.resources.join_dialog_field_label_code
import teamsoul.composeapp.generated.resources.join_dialog_title
import teamsoul.composeapp.generated.resources.main_button_create_room
import teamsoul.composeapp.generated.resources.main_button_join_room
import teamsoul.composeapp.generated.resources.main_subtitle
import teamsoul.composeapp.generated.resources.main_title

object MainScreen : Screen {
    @Composable
    override fun Content() {
        MainScreen()
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinInject()
) {
    val navigator = LocalNavigator.currentOrThrow
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is MainNavigationEvent.NavigateToGameSelection ->
                    navigator.push(GameSelectionScreen(event.roomId))

                is MainNavigationEvent.NavigateToGameWebView ->
                    navigator.push(GameWebViewScreen(event.url))
            }
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(message = uiState.error!!)
            viewModel.onErrorShown()
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
            Image(
                painterResource(Res.drawable.img_team_puzzle),
                modifier = Modifier.fillMaxWidth(1f).height(300.dp),
                alignment = Alignment.TopCenter,
                contentDescription = null,
            )
            MainContent(
                isLoading = uiState.isLoading,
                onJoinGameClicked = viewModel::onJoinGameClicked,
                onCreateRoomClicked = viewModel::onCreateRoomClicked
            )
        }
    }

    if (uiState.showJoinGameDialog) {
        JoinGameDialog(
            isLoading = uiState.isLoading,
            onConfirm = viewModel::onJoinRoomConfirm,
            onDismiss = viewModel::onJoinGameDialogDismiss
        )
    }
}

@Composable
private fun MainContent(
    isLoading: Boolean,
    onJoinGameClicked: () -> Unit,
    onCreateRoomClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(Res.string.main_title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.main_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            lineHeight = 24.sp
        )

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onJoinGameClicked,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, TeamSoulBlue)
            ) {
                Text(
                    text = stringResource(Res.string.main_button_join_room),
                    color = TeamSoulBlue
                )
            }

            Button(
                onClick = onCreateRoomClicked,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = TeamSoulBlue)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.main_button_create_room)
                    )
                }
            }
        }
    }
}

@Composable
private fun JoinGameDialog(
    isLoading: Boolean,
    onConfirm: (roomId: String) -> Unit,
    onDismiss: () -> Unit
) {
    var roomCode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.join_dialog_title)) },
        text = {
            Column {
                Text(
                    text = stringResource(Res.string.join_dialog_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = roomCode,
                    onValueChange = { roomCode = it },
                    label = { Text(stringResource(Res.string.join_dialog_field_label_code)) },
                    singleLine = true,
                    enabled = !isLoading
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(roomCode) },
                enabled = roomCode.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text(stringResource(Res.string.join_dialog_button_join))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text(stringResource(Res.string.button_cancel))
            }
        }
    )
}
