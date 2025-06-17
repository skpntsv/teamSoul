package ru.nsu.teamsoul.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.nsu.teamsoul.data.remote.dto.AuthenticationResponse
import ru.nsu.teamsoul.data.remote.dto.LoginRequest
import ru.nsu.teamsoul.data.remote.dto.UserDetailsResponse

class AuthApi(private val client: HttpClient) {

    suspend fun login(loginRequest: LoginRequest): AuthenticationResponse {
        return client.post("/auth-service/api/v1/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }

    suspend fun validateToken(): UserDetailsResponse {
        val response = client.get("/auth-service/api/v1/validate")

        println(response.request.headers)
        println(response.bodyAsText())

        return response.body()
    }
}