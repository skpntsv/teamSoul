package ru.nsu.teamsoul.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val identifier: String,
    val password: String
)

@Serializable
data class AuthenticationResponseDto(
    @SerialName("access_token")
    val accessToken: String
)

@Serializable
data class UserDetailsResponseDto(
    val id: Int,
    val login: String,
    val role: String // "user", "admin", "hr"
)