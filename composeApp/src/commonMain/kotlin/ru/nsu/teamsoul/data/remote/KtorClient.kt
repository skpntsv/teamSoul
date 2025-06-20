package ru.nsu.teamsoul.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.nsu.teamsoul.data.local.AuthTokenRepository

fun createHttpClient(authTokenRepository: AuthTokenRepository): HttpClient {
    return HttpClient {

        defaultRequest {
            url("http://128.0.0.1:8888")
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            level = LogLevel.ALL

            logger = Logger.SIMPLE
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = authTokenRepository.getToken()
                    if (token != null && token.isNotBlank()) {
                        println("Token found: $token")
                        BearerTokens(accessToken = token, refreshToken = "")
                    } else {
                        println("No token found, returning null")
                        null
                    }
                }
            }
        }
    }
}