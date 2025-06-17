package ru.nsu.teamsoul.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatedRoomDto(
    val roomId: Int
)

@Serializable
data class UserConnectDto(
    val url: String
)

@Serializable
data class UserConnectRequest(
    val roomId: String
)

@Serializable
data class CreateGameInRoomRequest(
    val id: Int,
    val roomId: Int
)