package org.example.projects

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform