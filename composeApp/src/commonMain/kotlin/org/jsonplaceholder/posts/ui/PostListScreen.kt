package org.jsonplaceholder.posts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jsonplaceholder.posts.domain.Post

@Composable
fun PostListScreen(viewModel: PostViewModel) {
    var userIdInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadNextPage()
    }

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
                        viewModel.loadNextPage(userIdInput.toIntOrNull(), isNewSearch = true)
                    }
                }) {
                    Icon(Icons.Default.Search, "Buscar")
                }
            }
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(viewModel.posts) { post ->
                PostItem(post)

                LaunchedEffect(viewModel.posts.size) {
                    if (post == viewModel.posts.last()) {
                        viewModel.loadNextPage(userIdInput.toIntOrNull())
                    }
                }
            }

            if (viewModel.isLoading) {
                item { CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally)) }
            }
        }
    }
}