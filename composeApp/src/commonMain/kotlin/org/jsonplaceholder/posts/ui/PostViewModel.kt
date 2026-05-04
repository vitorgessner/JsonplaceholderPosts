package org.jsonplaceholder.posts.ui

import androidx.compose.runtime.*
import org.jsonplaceholder.posts.domain.Post
import org.jsonplaceholder.posts.domain.PostService

class PostViewModel(private val service: PostService) {
    var posts = mutableStateListOf<Post>()
    var currentPage by mutableStateOf(1)
    var isEndReached by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    suspend fun loadNextPage(userId: Int? = null, isNewSearch: Boolean = false) {
        if (isLoading || (isEndReached && !isNewSearch)) return

        if (isNewSearch) {
            posts.clear()
            currentPage = 1
            isEndReached = false
        }

        isLoading = true
        val newPosts = service.getPosts(currentPage, 10, userId)

        if (newPosts.isEmpty()) {
            isEndReached = true
        } else {
            posts.addAll(newPosts)
            currentPage++
        }
        isLoading = false
    }
}