package ru.nsu.teamsoul.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import ru.nsu.teamsoul.data.remote.api.GamePluginsApi
import ru.nsu.teamsoul.data.remote.api.GameRoomApi
import ru.nsu.teamsoul.data.remote.dto.CreateGameInRoomRequest
import ru.nsu.teamsoul.data.remote.dto.CreateGameRequest
import ru.nsu.teamsoul.data.remote.dto.GamePluginsResponse
import ru.nsu.teamsoul.data.remote.dto.UserConnectRequest

class GameRepositoryImpl(
    private val gamePluginsApi: GamePluginsApi,
    private val gameRoomApi: GameRoomApi
) : GameRepository {

    override suspend fun getGamesList(): Result<List<GamePluginsResponse>> {
        return try {
            val games = gamePluginsApi.getGamesList()
            Result.success(games)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound) {
                Result.failure(Exception("Список игр не найден"))
            } else {
                Result.failure(Exception("Произошла ошибка при загрузке игр"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Произошла ошибка на сервере"))
        }
    }

    override suspend fun createGameInstance(gameId: Long, roomId: Long): Result<String> {
        return try {
            val request = CreateGameRequest(id = gameId, roomId = roomId)
            val containerId = gamePluginsApi.createGameInstance(request)
            Result.success(containerId)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.BadRequest) {
                val errorBody = e.response.bodyAsText()
                Result.failure(Exception(errorBody.ifBlank { "Ошибка в запросе клиента" }))
            } else {
                Result.failure(Exception("Произошла ошибка при создании игры"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Произошла ошибка на сервере"))
        }
    }

    override suspend fun createRoom(): Result<Int> {
        return try {
            val createdRoom = gameRoomApi.createRoom()
            Result.success(createdRoom.roomId)
        } catch (e: Exception) {
            println("GameRepo :: createRoom failed: ${e.message}")
            Result.failure(Exception("Не удалось создать комнату"))
        }
    }

    override suspend fun joinRoom(roomId: String): Result<String> {
        return try {
            val request = UserConnectRequest(roomId = roomId)
            val connectionInfo = gameRoomApi.joinRoom(request)
            Result.success(connectionInfo.url)
        } catch (e: Exception) {
            println("GameRepo :: joinRoom failed: ${e.message}")
            Result.failure(Exception("Не удалось подключиться к комнате"))
        }
    }

    override suspend fun createGameInRoom(gameId: Int, roomId: Int): Result<String> {
        return try {
            val request = CreateGameInRoomRequest(id = gameId, roomId = roomId)
            val connectionInfo = gameRoomApi.createGameInRoom(request)
            Result.success(connectionInfo.url)
        } catch (e: Exception) {
            println("GameRepo :: createGameInRoom failed: ${e.message}")
            Result.failure(Exception("Не удалось создать игру в комнате"))
        }
    }
}