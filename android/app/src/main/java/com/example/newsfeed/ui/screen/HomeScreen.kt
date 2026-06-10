package com.example.newsfeed.ui.screen

import androidx.compose.ui.graphics.Color
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.ui.model.HomeUiState
import com.example.newsfeed.ui.model.HomeViewModel
import okio.Source


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
    viewModel: HomeViewModel = viewModel()
            ,onArticleClick: (ArticleDto) -> Unit
){
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HomeHeader()

        Spacer(modifier = Modifier.height(16.dp))

        SourceSection(
            sources = sources,
            onSourceSelected = { viewModel.onSourceSelected(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CategorySection(
            categories = categories,
            onCategorySelected = { viewModel.onCategorySelected(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ArticleList(
            uiState = uiState,
            onArticleClick = onArticleClick
        )
    }
}

//Header
@Composable
fun HomeHeader(){
    Text(
        text = "News Feed",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

//Nguồn báo
@Composable
fun SourceSection(
    sources: List<Pair<String, Int?>>,
    onSourceSelected: (Int?) -> Unit
){
    Column {
        Text(
            text = "Nguồn báo" ,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sources){
                (name,id) ->
                FilterChip(
                onClick = { onSourceSelected(id) },
                label = { Text(name) },
                    selected = false,
                    modifier = Modifier.animateContentSize()
                )
            }
        }
    }
}

//Chuyên mục
@Composable
fun CategorySection(
    categories : List<Pair<String, String?>>,
    onCategorySelected: (String?) -> Unit
){
    Column {
        Text(
            text = "Chuyên mục",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { (name, slug) ->
                FilterChip(
                    onClick = { onCategorySelected(slug) },
                    selected = false,
                    label = { Text(name) }
                )
            }
        }
    }
}

//DANH SÁCH ARTICLE
@Composable
fun ArticleList(
    uiState: HomeUiState,
    onArticleClick: (ArticleDto) -> Unit
){
    when{
        uiState.isLoading->{
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            ErrorView(error = uiState.error)
        }

        uiState.articles.isEmpty() -> {
            EmptyView()
        }else->{
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    items= uiState.articles,
                    key = {it.id}
                ){
                    article ->ArticleCard(article = article,
                        onClick = {onArticleClick(article)})
                }
            }
        }
    }

}

//Thẻ Article ( thẻ bài báo)
@Composable
fun ArticleCard(
    article: ArticleDto,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Thêm ảnh đại diện nếu có


            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Chip(
                        text = article.sourceName,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                    Chip(
                        text = article.category,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        }
    }
}


// Components hỗ trợ
@Composable
fun Chip(
    text: String,
    containerColor: Color
) {
    AssistChip(
        onClick = {},
        label = {
            Text(text)
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor
        ),
        modifier = Modifier.height(32.dp)
    )
}
@Composable
fun ErrorView(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Article,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Không có bài viết nào",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
//@Composable
//fun HomeScreen(
//    viewModel: HomeViewModel = viewModel(),
//    onArtcleClick : (ArticleDto) ->Unit
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "NEWS FEED",
//            style = MaterialTheme.typography.headlineSmall
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        // Nguồn báo
//        Text("Nguồn báo")
//
//        LazyRow {
//            items(sources) { (name, id) ->
//                Button(
//                    onClick = {
//                        viewModel.onSourceSelected(id)
//                    }
//                ) {
//                    Text(name)
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Chuyên mục
//        Text("Chuyên mục")
//
//        LazyRow {
//            items(categories) { (name, slug) ->
//                Button(
//                    onClick = {
//                        viewModel.onCategorySelected(slug)
//                    }
//                ) {
//                    Text(name)
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        when {
//
//            uiState.isLoading -> {
//                Text("Đang tải dữ liệu...")
//            }
//
//            uiState.error != null -> {
//                Text("Lỗi: ${uiState.error}")
//            }
//
//            else -> {
//                LazyColumn {
//
//                    items(uiState.articles) { article ->
//
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp),
//                            onClick = {
//                                onArtcleClick(article)
//                            }
//                        ) {
//
//                            Column(
//                                modifier = Modifier.padding(12.dp)
//                            ) {
//
//                                Text(
//                                    text = article.title,
//                                    style = MaterialTheme.typography.titleMedium
//                                )
//
//                                Spacer(
//                                    modifier = Modifier.height(4.dp)
//                                )
//
//                                Text(
//                                    text = article.sourceName
//                                )
//
//                                Text(
//                                    text = article.category
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
//
//    }
//}