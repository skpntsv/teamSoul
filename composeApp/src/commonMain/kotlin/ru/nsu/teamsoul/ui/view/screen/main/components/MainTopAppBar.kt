package ru.nsu.teamsoul.ui.view.screen.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import ru.nsu.teamsoul.ui.components.AppHeader
import ru.nsu.teamsoul.ui.view.screen.main.MainHeaderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    viewModel: MainHeaderViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    CenterAlignedTopAppBar(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        navigationIcon = { Spacer(Modifier.fillMaxWidth(0.3f)) },
        title = { AppHeader() },
        actions = {
            uiState.username?.let { username ->
                TextButton(
                    onClick = viewModel::onLogoutClicked,
                    modifier = Modifier.fillMaxWidth(0.3f)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.fillMaxWidth(0.1f))
                    Text(
                        text = username,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )

    if (uiState.showLogoutDialog) {
        LogoutDialog(
            onConfirm = viewModel::onLogoutConfirm,
            onDismiss = viewModel::onLogoutDialogDismiss
        )
    }
}