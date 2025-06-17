package ru.nsu.teamsoul.data.repository

import ru.nsu.teamsoul.data.remote.dto.UserDetailsResponseDto


interface AuthRepository {
    suspend fun login(identifier: String, password: String): Boolean
    suspend fun logout()

    suspend fun isLoggedIn(): Boolean

    suspend fun getUserDetails(): UserDetailsResponseDto?
}