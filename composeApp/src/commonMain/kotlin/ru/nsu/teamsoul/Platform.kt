package ru.nsu.teamsoul

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform