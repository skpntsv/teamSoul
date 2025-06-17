package ru.nsu.teamsoul.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.nsu.teamsoul.data.remote.dto.CreateGameRequest
import ru.nsu.teamsoul.data.remote.dto.GamePluginsResponse

class GamePluginsApi(private val client: HttpClient) {

    suspend fun getGamesList(): List<GamePluginsResponse> {
        return client.get("/game-plugins-service/api/v1/games").body()
    }

    suspend fun createGameInstance(requestBody: CreateGameRequest): String {
        return client.post("/game-plugins-service/api/v1/games/create") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }
}