package com.example.newsfeed.ui.model

import android.app.Application
import android.graphics.ColorSpace
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.newsfeed.data.repository.SavedRepository

class SavedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SavedRepository(application)
    val getArticles = repository.getSavedArticles()
}