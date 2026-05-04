package org.jsonplaceholder.posts.domain

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.jsonplaceholder.posts.domain.Post

class PostService {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                coerceInputValues = true
            })
        }
    }

    suspend fun getPosts(page: Int, limit: Int, userId: Int? = null): List<Post> {
        return httpClient.get("https://jsonplaceholder.typicode.com/posts") {
            url {
                parameters.append("_page", page.toString())
                parameters.append("_limit", limit.toString())
                userId?.let {
                    parameters.append("userId", it.toString())
                }
            }
        }.body()
    }
}