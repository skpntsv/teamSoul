package ru.nsu.teamsoul.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequestDto(
    val username: String,
    val email: String,
    val password: String,
    val team: String? = null,
    val role: String
)

@Serializable
data class UserResponseDto(
    val id: String,
    val username: String,
    val email: String,
    val team: String? = null,
    val role: String,
    val created_at: String,
    val updated_at: String
)