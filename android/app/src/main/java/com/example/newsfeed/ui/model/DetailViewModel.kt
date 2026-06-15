package com.example.newsfeed.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.worker.ArticleContentScraper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _content = MutableStateFlow<String?>(null)
    val content: StateFlow<String?> = _content.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var lastUrl: String? = null

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
}
