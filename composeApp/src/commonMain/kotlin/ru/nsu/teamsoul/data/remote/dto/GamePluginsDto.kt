package ru.nsu.teamsoul.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateGameRequest(
    val id: Long,
    val roomId: Long
)

@Serializable
data class GamePluginsResponse(
    val id: Long,
    val name: String,
    val version: String,
    val description: String? = null,
)