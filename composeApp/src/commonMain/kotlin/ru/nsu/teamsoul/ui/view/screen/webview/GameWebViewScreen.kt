package ru.nsu.teamsoul.ui.view.screen.webview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import org.jetbrains.compose.resources.stringResource
import ru.nsu.teamsoul.ui.components.AppHeader
import teamsoul.composeapp.generated.resources.Res
import teamsoul.composeapp.generated.resources.button_cancel

data class GameWebViewScreen(val url: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val webViewState = rememberWebViewState(url = url)

        Scaffold(
            containerColor = Color.White,
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
                    actions = { Spacer(Modifier.fillMaxWidth(0.1f)) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
        ) { paddingValues ->
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
        }
    }
}