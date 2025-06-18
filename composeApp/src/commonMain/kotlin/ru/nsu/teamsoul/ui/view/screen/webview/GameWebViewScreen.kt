package ru.nsu.teamsoul.ui.view.screen.webview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import org.jetbrains.compose.resources.stringResource
import teamsoul.composeapp.generated.resources.Res
import teamsoul.composeapp.generated.resources.button_cancel
import teamsoul.composeapp.generated.resources.game_webview_title_loading

data class GameWebViewScreen(val url: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val webViewState = rememberWebViewState(url = url)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = webViewState.pageTitle ?: stringResource(Res.string.game_webview_title_loading),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.button_cancel)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
        }
    }
}