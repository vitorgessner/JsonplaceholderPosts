package org.jsonplaceholder.posts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PostListScreen(viewModel: PostViewModel) {
    var userIdInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    val state by viewModel.uiState.collectAsState()

    Column {
        TextField(
            value = userIdInput,
            onValueChange = { userIdInput = it },
            label = { Text("Filtrar por User ID") },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                IconButton(onClick = {
                    scope.launch {
                        viewModel.loadPosts(userIdInput.toIntOrNull(), isRefresh = true)
                    }
                }) {
                    Icon(Icons.Default.Search, "Buscar")
                }
            }
        )

        when (val current = state) {
            is PostUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

            is PostUiState.Error -> {
                Text(current.message, color = Color.Red)
                Button(onClick = {
                    scope.launch {
                        viewModel.loadPosts(isRefresh = true)
                    }
                }) {
                    Text("Tentar novamente")
                }
            }

            is PostUiState.Success -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(current.posts) { post ->
                        PostItem(post)
                        if (post == current.posts.last()) {
                            LaunchedEffect(post.id) { viewModel.loadPosts() }
                        }
                    }
                }
            }
            is PostUiState.Empty -> Text("Nenhum post encontrado.")
        }
    }
}