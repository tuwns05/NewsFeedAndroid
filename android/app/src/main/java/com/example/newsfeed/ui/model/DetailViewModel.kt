package com.example.newsfeed.ui.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.data.database.AppDatabase
import com.example.newsfeed.data.entity.SavedArticle
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.repository.SavedRepository
import com.example.newsfeed.worker.ArticleContentScraper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _content = MutableStateFlow<String?>(null)

    val content: StateFlow<String?> = _content.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var lastUrl: String? = null

    private val repository = SavedRepository(application.applicationContext)

    val _isSaved = MutableStateFlow(false)

    private var savedJob: kotlinx.coroutines.Job? = null
    fun loadArticleContent(url: String?) {

        if (url.isNullOrBlank()) {
            _content.value = "Không có liên kết bài báo."
            return
        }
        if (url == lastUrl) return // tránh load lại nhiều lần
        lastUrl = url
        viewModelScope.launch {
            _isLoading.value = true
            _content.value = ArticleContentScraper.fetchArticleContent(url)
            _isLoading.value = false
        }
    }

    // Theo dõi trạng thái bookmark
    fun observeSavedState(articleId: Int) {
        savedJob?.cancel() // hủy job cũ nếu có
        savedJob = viewModelScope.launch {
            repository.isSaved(articleId).collect { saved ->
                _isSaved.value = saved
            }
        }
    }

    // Toggle lưu/bỏ lưu
    fun toggleSave(article: ArticleDto, content: String) {
        viewModelScope.launch {
            if (_isSaved.value) {
                repository.unsave(article.id)
            } else {
                repository.save(article, content)
            }
        }
    }
}
