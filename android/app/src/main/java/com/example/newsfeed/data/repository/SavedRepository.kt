package com.example.newsfeed.data.repository

import android.content.Context
import com.example.newsfeed.data.dao.SavedArticleDao
import com.example.newsfeed.data.database.AppDatabase
import com.example.newsfeed.data.entity.SavedArticle
import com.example.newsfeed.data.entity.toArticleDto
import com.example.newsfeed.data.entity.toSavedArticle
import com.example.newsfeed.data.remote.dto.ArticleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedRepository(context: Context){
    private val savedArticleDao: SavedArticleDao =
        AppDatabase.getDatabase(context).savedArticleDao()

    // Lưu bookmark
    suspend fun save(article: ArticleDto, content: String) =
        savedArticleDao.insert(article.toSavedArticle(content))

    // Xóa bookmark
    suspend fun unsave(articleId: Int) =
        savedArticleDao.deleteById(articleId)

    // Kiểm tra đã lưu chưa
    fun isSaved(articleId: Int): Flow<Boolean> =
        savedArticleDao.isSaved(articleId)

    // Lấy bài offline theo id
    suspend fun getOfflineArticle(id: Int): SavedArticle? =
        savedArticleDao.getById(id)

    // lấy tất cả bài of
    fun getSavedArticles(): Flow<List<SavedArticle>> =
        savedArticleDao.getAll()
}