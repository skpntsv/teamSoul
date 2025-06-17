package ru.nsu.teamsoul.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import io.ktor.client.*
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module
import org.koin.core.module.dsl.factoryOf
import ru.nsu.teamsoul.data.local.AuthTokenRepository
import ru.nsu.teamsoul.data.local.AuthTokenRepositoryImpl
import ru.nsu.teamsoul.data.remote.createHttpClient
import ru.nsu.teamsoul.data.remote.api.*
import ru.nsu.teamsoul.data.repository.*
import ru.nsu.teamsoul.ui.view.screen.login.LoginViewModel
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

    // ViewModels
    factoryOf(::SplashViewModel)
    factoryOf(::LoginViewModel)
//    factoryOf(::MainViewModel)
}