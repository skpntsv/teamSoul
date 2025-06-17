package ru.nsu.teamsoul.ui.view.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.nsu.teamsoul.ui.components.AppHeader
import ru.nsu.teamsoul.ui.components.ResponsiveLayout
import ru.nsu.teamsoul.ui.theme.TeamSoulBlue
import ru.nsu.teamsoul.ui.theme.TextPrimary
import ru.nsu.teamsoul.ui.theme.TextSecondary
import teamsoul.composeapp.generated.resources.Res
import teamsoul.composeapp.generated.resources.connect_to_game_button
import teamsoul.composeapp.generated.resources.create_room_button
import teamsoul.composeapp.generated.resources.description_main_menu
import teamsoul.composeapp.generated.resources.img_team_puzzle
import teamsoul.composeapp.generated.resources.label_main_menu

class MainScreen : Screen {

    @Composable
    override fun Content() {
        Scaffold { paddingValues ->
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
                MainContent()
            }
        }
    }
}

@Composable
private fun MainContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.label_main_menu),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.description_main_menu),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { TODO("Логика присоединения к игре") },
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, TeamSoulBlue)
            ) {
                Text(stringResource(Res.string.connect_to_game_button), color = TeamSoulBlue)
            }
            Button(
                onClick = { TODO("Логика создания комнаты") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = TeamSoulBlue)
            ) {
                Text(stringResource(Res.string.create_room_button))
            }
        }
    }
}