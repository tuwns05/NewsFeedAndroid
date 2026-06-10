package com.example.newsfeed.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.ui.model.HomeViewModel


// Danh sách chuyên mục
private val categories = listOf(
    "Mới nhất"  to null,
    "Công nghệ" to "cong-nghe",
    "Thể thao"  to "the-thao",
    "Giáo dục"  to "giao-duc",
    "Giải trí"  to "giai-tri",
    "Thế giới"  to "the-gioi"
)

// Danh sách nguồn báo
private val sources = listOf(
    "Tất cả"    to null,
    "VnExpress" to 1,
    "Tuổi Trẻ"  to 2,
    "Thanh Niên" to 3
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onArtcleClick : (ArticleDto) ->Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "NEWS FEED",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Nguồn báo
        Text("Nguồn báo")

        LazyRow {
            items(sources) { (name, id) ->
                Button(
                    onClick = {
                        viewModel.onSourceSelected(id)
                    }
                ) {
                    Text(name)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chuyên mục
        Text("Chuyên mục")

        LazyRow {
            items(categories) { (name, slug) ->
                Button(
                    onClick = {
                        viewModel.onCategorySelected(slug)
                    }
                ) {
                    Text(name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {

            uiState.isLoading -> {
                Text("Đang tải dữ liệu...")
            }

            uiState.error != null -> {
                Text("Lỗi: ${uiState.error}")
            }

            else -> {
                LazyColumn {

                    items(uiState.articles) { article ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = {
                                onArtcleClick(article)
                            }
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    text = article.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = article.sourceName
                                )

                                Text(
                                    text = article.category
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}