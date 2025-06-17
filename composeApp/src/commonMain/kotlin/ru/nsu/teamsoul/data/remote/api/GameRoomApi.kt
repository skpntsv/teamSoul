package ru.nsu.teamsoul.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.nsu.teamsoul.data.remote.dto.CreateGameInRoomRequest
import ru.nsu.teamsoul.data.remote.dto.CreatedRoomDto
import ru.nsu.teamsoul.data.remote.dto.UserConnectDto
import ru.nsu.teamsoul.data.remote.dto.UserConnectRequest


class GameRoomApi(private val client: HttpClient) {

    suspend fun createRoom(): CreatedRoomDto {
        return client.get("/game-room-service/api/v1/room/create").body()
    }

    suspend fun joinRoom(requestBody: UserConnectRequest): UserConnectDto {
        return client.post("/game-room-service/api/v1/user/connect") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }

    suspend fun createGameInRoom(requestBody: CreateGameInRoomRequest): UserConnectDto {
        return client.post("/game-room-service/api/v1/game/create") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }
}