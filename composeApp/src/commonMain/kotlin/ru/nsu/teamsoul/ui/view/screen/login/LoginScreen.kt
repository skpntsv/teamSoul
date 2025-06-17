package ru.nsu.teamsoul.ui.view.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.stringResource
import ru.nsu.teamsoul.ui.components.AppHeader
import ru.nsu.teamsoul.ui.components.ResponsiveLayout
import ru.nsu.teamsoul.ui.theme.TeamSoulBlue
import ru.nsu.teamsoul.ui.theme.TextPrimary
import ru.nsu.teamsoul.ui.view.screen.main.MainScreen
import teamsoul.composeapp.generated.resources.Res
import teamsoul.composeapp.generated.resources.login_button_text
import teamsoul.composeapp.generated.resources.login_input_email
import teamsoul.composeapp.generated.resources.login_input_password
import teamsoul.composeapp.generated.resources.login_screen_title

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is LoginEvent.NavigateToMain -> navigator.replaceAll(MainScreen())
                }
            }
        }

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                AppHeader()
                ResponsiveLayout(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                    mobileContent = {
                        LoginContent(
                            uiState,
                            viewModel::onIdentifierChange,
                            viewModel::onPasswordChange,
                            viewModel::login,
                            true
                        )
                    },
                    desktopContent = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                // Image(painterResource("drawable/login_illustration.xml"), contentDescription = null)
                                Spacer(modifier = Modifier.fillMaxSize(0.8f))
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                LoginContent(
                                    uiState,
                                    viewModel::onIdentifierChange,
                                    viewModel::onPasswordChange,
                                    viewModel::login,
                                    false
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    isMobile: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isMobile) Modifier.verticalScroll(rememberScrollState()) else Modifier),
        horizontalAlignment = if (isMobile) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        if (isMobile) {
            // Иллюстрация для мобильной версии
            Box(modifier = Modifier.fillMaxWidth(0.8f).height(200.dp)) {
                // Image(painterResource("drawable/login_illustration.xml"), contentDescription = null)
                Spacer(modifier = Modifier.fillMaxSize()) // Заглушка
            }
            Spacer(Modifier.height(32.dp))
        }

        Text(
            text = stringResource(Res.string.login_screen_title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.identifier,
            onValueChange = onLoginChanged,
            label = { Text(stringResource(Res.string.login_input_email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.error != null
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            label = { Text(stringResource(Res.string.login_input_password)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.error != null,
            visualTransformation = PasswordVisualTransformation()
        )
        if (uiState.error != null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = TeamSoulBlue)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text(stringResource(Res.string.login_button_text))
            }
        }
    }
}