package ru.nsu.teamsoul.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.nsu.teamsoul.data.local.AuthTokenRepository
import ru.nsu.teamsoul.data.local.AuthTokenRepositoryImpl
import ru.nsu.teamsoul.data.remote.api.AuthApi
import ru.nsu.teamsoul.data.remote.api.GamePluginsApi
import ru.nsu.teamsoul.data.remote.api.GameRoomApi
import ru.nsu.teamsoul.data.remote.api.UserApi
import ru.nsu.teamsoul.data.remote.createHttpClient
import ru.nsu.teamsoul.data.repository.AuthRepository
import ru.nsu.teamsoul.data.repository.AuthRepositoryImpl
import ru.nsu.teamsoul.data.repository.GameRepository
import ru.nsu.teamsoul.data.repository.GameRepositoryImpl
import ru.nsu.teamsoul.ui.view.screen.gameselection.GameSelectionViewModel
import ru.nsu.teamsoul.ui.view.screen.login.LoginViewModel
import ru.nsu.teamsoul.ui.view.screen.main.MainViewModel
import ru.nsu.teamsoul.ui.view.screen.splash.SplashViewModel

@OptIn(ExperimentalSettingsApi::class)
val commonModule = module {
    // Settings
    single<SuspendSettings> { Settings().toSuspendSettings() }

    // Network
    single<HttpClient> { createHttpClient(get()) }
    singleOf(::AuthApi)
    singleOf(::UserApi)
    singleOf(::GamePluginsApi)
    singleOf(::GameRoomApi)

    singleOf(::AuthTokenRepositoryImpl) { bind<AuthTokenRepository>() }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::GameRepositoryImpl) { bind<GameRepository>() }

    // ViewModels
    factoryOf(::SplashViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::MainViewModel)
    factoryOf(::GameSelectionViewModel)
}