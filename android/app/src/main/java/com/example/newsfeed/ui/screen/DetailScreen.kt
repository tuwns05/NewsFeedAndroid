package com.example.newsfeed.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsfeed.data.remote.dto.ArticleDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: ArticleDto?,
    onBack: () -> Unit
) {
    // Kiểm tra article null
    if (article == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Không có dữ liệu bài báo")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết bài báo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Nguồn báo
            Text(
                text = article.sourceName,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tiêu đề
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chuyên mục & thời gian
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📂 ${article.category}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (article.publishedAt.isNotEmpty()) {
                    Text(
                        text = "📅 ${formatDate(article.publishedAt)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Đường kẻ
            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            // Nội dung
            Text(
                //article.content ?: article.description ?:
                text =  "Đang cập nhật...",
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 28.sp
            )
        }
    }
}

// Format ngày tháng đơn giản
private fun formatDate(dateString: String): String {
    return if (dateString.length >= 10) {
        dateString.substring(0, 10) // Lấy "YYYY-MM-DD"
    } else {
        dateString
    }
}