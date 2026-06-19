package com.example.newsfeed.ui.screen

import android.R.attr.content
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.ui.model.DetailViewModel
import com.example.newsfeed.ui.model.HomeViewModel
import java.io.Console

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: ArticleDto?,
    onBack: () -> Unit,
    offlineContent: String? = null,
    viewModel: DetailViewModel = viewModel()
) {
    val context = LocalContext.current

    val isSaved by viewModel._isSaved.collectAsStateWithLifecycle()

    val content by viewModel.content.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val displayContent = offlineContent ?: content

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
    // Gọi cào nội dung khi article.url thay đổi
    LaunchedEffect(article.link) {
        // Chỉ fetch mạng nếu không có offline content
        if (offlineContent == null) {
            viewModel.loadArticleContent(article.link)
        }
    }

    // Observe trạng thái bookmark
    LaunchedEffect(article.id) {
        viewModel.observeSavedState(article.id)
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
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
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
                    if (article.publishedAt.isNotEmpty()) {
                        Text(
                            text = "📅 ${formatDate(article.publishedAt)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    //NÚT LƯU
                    IconButton(

                        onClick = {
                            Log.d("DETAIL", "Content = $content")
                            println("Content = $content")
                            viewModel.toggleSave(article,displayContent?: "Không có nội dung" )
                        }) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark
                            else Icons.Default.BookmarkBorder,
                            contentDescription = if (isSaved) "Bỏ lưu" else "Lưu bài báo",
                            tint = if (isSaved) MaterialTheme.colorScheme.primary
                            else LocalContentColor.current
                        )}


                    //nút share
                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.link}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ bài viết qua:")
                            context.startActivity(shareIntent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Chia sẻ bài báo"
                        )
                    }
                    //nút mở bài báo qua link gốc
                    IconButton(
                        onClick = {
                            try {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                                context.startActivity(browserIntent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Mở nguồn gốc"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Đường kẻ
                HorizontalDivider()
                //ẢNH
                if (!article.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(article.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Article,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Nội dung
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Text(
                        text = displayContent?: "Không có nội dung",
                        style = MaterialTheme.typography.bodyLarge
                    )

                }

            }
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