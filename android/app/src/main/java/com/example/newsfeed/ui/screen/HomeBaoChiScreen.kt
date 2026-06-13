package com.example.newsfeed.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.repository.ArticleRepository
import com.example.newsfeed.ui.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// --- Dữ liệu nguồn báo (có thể lấy từ API, ở đây giữ nguyên như cũ) ---
private val sources = listOf(
    "Tất cả" to null,
    "VnExpress" to 1,
    "Tuổi Trẻ" to 2,
    "Thanh Niên" to 3
)

// --- UI State riêng cho màn hình Báo chí ---
data class BaoChiUiState(
    val articles: List<ArticleDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedSourceId: Int? = null
)

// --- ViewModel riêng, sử dụng ArticleRepository có sẵn ---
class BaoChiViewModel : ViewModel() {
    private val repository = ArticleRepository()
    private val _uiState = MutableStateFlow(BaoChiUiState())
    val uiState: StateFlow<BaoChiUiState> = _uiState.asStateFlow()

    init {
        loadArticlesForSource(null)
    }

    fun selectSource(sourceId: Int?) {
        _uiState.value = _uiState.value.copy(selectedSourceId = sourceId)
        loadArticlesForSource(sourceId)
    }

    private fun loadArticlesForSource(sourceId: Int?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.getArticles(category = null, sourceId = sourceId)
            _uiState.value = _uiState.value.copy(
                articles = result.getOrElse { emptyList() },
                isLoading = false,
                error = result.exceptionOrNull()?.message
            )
        }
    }

    fun refresh() {
        loadArticlesForSource(_uiState.value.selectedSourceId)
    }
}

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
            sources = sources,
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