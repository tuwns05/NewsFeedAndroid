package com.example.newsfeed.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.remote.dto.SourceDto
import com.example.newsfeed.data.repository.ArticleRepository
import com.example.newsfeed.ui.model.BaoChiViewModel
import com.example.newsfeed.ui.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// --- Màn hình Báo chí chính, tái sử dụng SourceSection và ArticleList ---
@Composable
fun BaoChiScreen(
    viewModel: BaoChiViewModel = viewModel(),
    onArticleClick: (ArticleDto) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Tái sử dụng SourceSection có sẵn trong HomeScreen.kt
        SourceSection(
            sources = uiState.sources ,
            selectedSource = uiState.selectedSourceId,
            onSourceSelected = { sourceId ->
                viewModel.selectSource(sourceId)
            }
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Tái sử dụng ArticleList có sẵn trong HomeScreen.kt
        // (ArticleList đã xử lý loading, error, empty và hiển thị ArticleCard)
        ArticleList(
            uiState = HomeUiState(
                articles = uiState.articles,
                isLoading = uiState.isLoading,
                error = uiState.error
                // các trường khác không dùng đến
            ),
            onArticleClick = onArticleClick
        )
    }
}

@Composable
fun SourceSection(
    sources: List<SourceDto>,
    onSourceSelected: (Int?) -> Unit,
    selectedSource: Int? = null
){
    Column {
        Text(
            text = "Nguồn báo" ,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sources) {source ->
                FilterChip(
                    onClick = { onSourceSelected(source.id) },
                    label = { Text(source.name + "-" + source.categoryName) },
                    selected = source.id == selectedSource
                )
            }
        }
    }
}