package org.jsonplaceholder.posts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform