package com.example.newsfeed.ui.model

import android.app.Application
import android.graphics.ColorSpace
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.data.entity.SavedArticle
import com.example.newsfeed.data.repository.AuthRepository
import com.example.newsfeed.data.repository.SavedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SavedRepository(application)
    private val authRepo = AuthRepository(application)
    //Lấy id user
    private val userId: String
        get() = authRepo.getCurrentUserId() ?: ""

    private val _savedArticles = MutableStateFlow<List<SavedArticle>>(emptyList())

    val savedArticles: StateFlow<List<SavedArticle>> = _savedArticles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSavedArticles()
    }

    fun loadSavedArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getSavedArticles(userId).collect { articles ->
                _savedArticles.value = articles
                _isLoading.value = false
            }
        }
    }
}