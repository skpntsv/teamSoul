package ru.nsu.teamsoul.data.remote.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.*
import ru.nsu.teamsoul.data.remote.dto.AuthenticationResponseDto
import ru.nsu.teamsoul.data.remote.dto.LoginRequestDto
import ru.nsu.teamsoul.data.remote.dto.UserDetailsResponseDto

class AuthApi(private val client: HttpClient) {

    suspend fun login(loginRequest: LoginRequestDto): AuthenticationResponseDto {
        return client.post("/auth-service/api/v1/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }

    suspend fun validateToken(): UserDetailsResponseDto {
        val response = client.get("/auth-service/api/v1/validate")

        println(response.request.headers)
        println(response.bodyAsText())

        return response.body()
    }
}