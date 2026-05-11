package org.jsonplaceholder.posts.ui

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jsonplaceholder.posts.domain.Post
import org.jsonplaceholder.posts.domain.PostService

sealed interface PostUiState {
    object Loading : PostUiState
    data class Success(val posts: List<Post>, val isPaginated: Boolean = false) : PostUiState
    data class Error(val message: String) : PostUiState
    object Empty : PostUiState
}

class PostViewModel(private val service: PostService) {
    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Empty)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    var allPosts = mutableListOf<Post>()
    var currentPage by mutableStateOf(1)
    var isEndReached by mutableStateOf(false)

    suspend fun loadPosts(userId: Int? = null, isRefresh: Boolean = false) {
        if (isRefresh) {
            allPosts.clear()
            currentPage = 1
            isEndReached = false
        }

        if (isEndReached) return

        if (allPosts.isEmpty()) _uiState.value = PostUiState.Loading

        try {
            val result = service.getPosts(currentPage, 10, userId)
            if (result.isEmpty()) {
                isEndReached = true
                if (allPosts.isEmpty()) _uiState.value = PostUiState.Empty
            } else {
                allPosts.addAll(result)
                _uiState.value = PostUiState.Success(allPosts.toList())
                currentPage++
            }
        } catch (e: Exception) {
            _uiState.value = PostUiState.Error(mapError(e))
        }
    }

    private fun mapError(e: Exception): String = when(e) {
        is io.ktor.client.plugins.ClientRequestException -> "Erro no servidor (404/400)"
        is io.ktor.client.plugins.ServerResponseException -> "Erro interno do servidor (500)"
        else -> "Falha de conexão. Verifique sua internet."
    }
}