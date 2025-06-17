package ru.nsu.teamsoul.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import ru.nsu.teamsoul.data.local.AuthTokenRepository
import ru.nsu.teamsoul.data.remote.api.AuthApi
import ru.nsu.teamsoul.data.remote.dto.LoginRequest
import ru.nsu.teamsoul.data.remote.dto.UserDetailsResponse

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenRepository: AuthTokenRepository
) : AuthRepository {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun login(identifier: String, password: String): Boolean {
        return try {
            val request = LoginRequest(identifier = identifier, password = password)
            val response = authApi.login(request)
            tokenRepository.saveToken(response.accessToken)
            true
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                println("AuthRepository: Invalid login or password.")
            } else {
                println("AuthRepository: Client error on login: ${e.message}")
            }
            false
        } catch (e: MissingFieldException) {
            println("AuthRepository: Serialization error - API response doesn't match expected DTO: ${e.message}")
            false
        } catch (e: Exception) {
            println("AuthRepository: Generic error on login: ${e.message}")
            false
        }
    }

    override suspend fun logout() {
        tokenRepository.clearToken()
    }

    override suspend fun isLoggedIn(): Boolean {
        val token = tokenRepository.getToken()
        if (token.isNullOrEmpty()) {
            println("AuthRepository: No token found, user is not logged in.")
            return false
        }
        return try {
            authApi.validateToken()
            true
        } catch (e: Exception) {
            println("AuthRepository: Token validation failed: ${e.message}")
            logout()
            false
        }
    }

    override suspend fun getUserDetails(): UserDetailsResponse? {
        return if (isLoggedIn()) {
            try {
                authApi.validateToken()
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }
}