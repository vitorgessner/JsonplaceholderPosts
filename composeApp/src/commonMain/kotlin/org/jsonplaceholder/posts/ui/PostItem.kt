package org.jsonplaceholder.posts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jsonplaceholder.posts.domain.Post

@Composable
fun PostItem(post: Post) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp)) // Bordas arredondadas
            .background(Color.White) // Fundo branco
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)) // Borda cinza clara
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "USER ID: ${post.userId}",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.title,
                style = TextStyle(
                    color = Color(0xFF212121),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = post.body,
                style = TextStyle(
                    color = Color(0xFF424242),
                    fontSize = 14.sp
                )
            )
        }
    }
}