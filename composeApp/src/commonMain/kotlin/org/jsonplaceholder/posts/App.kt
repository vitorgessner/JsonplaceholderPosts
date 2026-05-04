package org.jsonplaceholder.posts

import androidx.compose.runtime.*
import org.jsonplaceholder.posts.domain.PostService
import org.jsonplaceholder.posts.ui.PostListScreen
import org.jsonplaceholder.posts.ui.PostViewModel

@Composable
fun App() {
    val postService = remember { PostService() }
    val viewModel = remember { PostViewModel(postService) }

    PostListScreen(viewModel = viewModel)
}