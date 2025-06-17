package ru.nsu.teamsoul.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.SuspendSettings

@OptIn(ExperimentalSettingsApi::class)
class AuthTokenRepositoryImpl(
    private val settings: SuspendSettings
) : AuthTokenRepository {
    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token"
    }

    override suspend fun saveToken(token: String) {
        settings.putString(AUTH_TOKEN_KEY, token)
    }

    override suspend fun getToken(): String? {
        return settings.getStringOrNull(AUTH_TOKEN_KEY)
    }

    override suspend fun clearToken() {
        settings.remove(AUTH_TOKEN_KEY)
    }
}