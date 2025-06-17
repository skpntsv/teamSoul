package ru.nsu.teamsoul.data.repository

import ru.nsu.teamsoul.data.remote.dto.GamePluginsResponse

interface GameRepository {
    suspend fun getGamesList(): Result<List<GamePluginsResponse>>
    suspend fun createGameInstance(gameId: Long, roomId: Long): Result<String>

    suspend fun createRoom(): Result<Long>
    suspend fun joinRoom(roomId: String): Result<String>
    suspend fun createGameInRoom(gameId: Long, roomId: Long): Result<String>
}