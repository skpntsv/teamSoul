package ru.nsu.teamsoul.data.local

interface AuthTokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}
