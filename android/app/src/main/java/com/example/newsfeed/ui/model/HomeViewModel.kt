package com.example.newsfeed.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.repository.ArticleRepository
import com.example.newsfeed.data.repository.SavedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//Lưu lại trang thái của màn hình home
data class HomeUiState(
    val articles: List<ArticleDto> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val selectedCategory: String? = null,
    val selectedSourceId: Int? = null,
    val readArticleIds: Set<Int> = emptySet()
)

class HomeViewModel() : ViewModel(){
    private val repository= ArticleRepository()
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadArticles()

    }

    fun loadArticles(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                error = null
            )
            val result = repository.getArticles(
                category = _uiState.value.selectedCategory,
                sourceId = _uiState.value.selectedSourceId
            )
            _uiState.value = _uiState.value.copy(
                articles = result.getOrElse { emptyList() },
                isLoading = false,
                isRefreshing = false,
                error = result.exceptionOrNull()?.message
            )
        }
    }

    fun onCategorySelected(slug: String?) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = slug,
            selectedSourceId = null
        )
        loadArticles()
    }

    fun onSourceSelected(sourceId: Int?) {
        _uiState.value = _uiState.value.copy(
            selectedSourceId = sourceId,
            selectedCategory = null
        )
        loadArticles()
    }

    //Refresh cập nhật lại tin tức
    fun refreshNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            //.NET fetch RSS mới
            repository.refreshFromServer()

            //lấy lại danh sách mới nhất
            val result = repository.getArticles(
                category = _uiState.value.selectedCategory,
                sourceId = _uiState.value.selectedSourceId
            )

            _uiState.value = _uiState.value.copy(
                articles     = result.getOrElse { _uiState.value.articles },
                isRefreshing = false,
                error        = result.exceptionOrNull()?.message
            )
        }
    }

    //Lấy chi tiết bài báo theo id
    suspend fun getArticleById(articleId: Int): ArticleDto? {
        return try {
            //tìm trong danh sách hiện tại trước
            val existingArticle = _uiState.value.articles.find { it.id == articleId }
            if (existingArticle != null) {
                return existingArticle
            }

            // Nếu không có, gọi API lấy chi tiết
            val result = repository.getArticleById(articleId)
            result.getOrNull()
        } catch (e: Exception) {
            null
        }
    }




}