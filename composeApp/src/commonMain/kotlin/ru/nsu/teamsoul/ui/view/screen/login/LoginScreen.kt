package ru.nsu.teamsoul.ui.view.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import ru.nsu.teamsoul.ui.components.AppHeader
import ru.nsu.teamsoul.ui.view.screen.main.MainScreen
import teamsoul.composeapp.generated.resources.Res
import teamsoul.composeapp.generated.resources.login_button_signin
import teamsoul.composeapp.generated.resources.login_field_label_identifier
import teamsoul.composeapp.generated.resources.login_field_label_password
import teamsoul.composeapp.generated.resources.login_title

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        LoginScreen()
    }
}


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinInject()
) {
    val navigator = LocalNavigator.currentOrThrow
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.NavigateToMain -> navigator.replaceAll(MainScreen)
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            AppHeader()
            LoginContent(
                uiState,
                viewModel::onIdentifierChange,
                viewModel::onPasswordChange,
                viewModel::login
            )
        }
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.identifier,
            onValueChange = onLoginChanged,
            label = { Text(stringResource(Res.string.login_field_label_identifier)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.error != null
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            label = { Text(stringResource(Res.string.login_field_label_password)) },
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
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = stringResource(Res.string.login_button_signin),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}