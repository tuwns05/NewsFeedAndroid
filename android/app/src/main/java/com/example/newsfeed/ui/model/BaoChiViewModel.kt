package com.example.newsfeed.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.remote.dto.SourceDto
import com.example.newsfeed.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// --- UI State riêng cho màn hình Báo chí ---
data class BaoChiUiState(
    val articles: List<ArticleDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedSourceId: Int? = null,
    val sources : List<SourceDto> = emptyList()

)
class BaoChiViewModel : ViewModel() {
    private val repository = ArticleRepository()
    private val _uiState = MutableStateFlow(BaoChiUiState())
    val uiState: StateFlow<BaoChiUiState> = _uiState.asStateFlow()

    init {
        loadArticlesForSource(null)
        loadSources()

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
    //lấy source
    private fun loadSources() {
        viewModelScope.launch {
            val result = repository.getSources()
            _uiState.value = _uiState.value.copy(
                sources = result.getOrElse { emptyList() }
            )
        }
    }

    fun refresh() {
        loadArticlesForSource(_uiState.value.selectedSourceId)
    }
}