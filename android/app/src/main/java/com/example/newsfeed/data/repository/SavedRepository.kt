package com.example.newsfeed.data.repository

import com.example.newsfeed.data.dao.SavedArticleDao
import com.example.newsfeed.data.entity.SavedArticle
import com.example.newsfeed.data.entity.toArticleDto
import com.example.newsfeed.data.entity.toSavedArticle
import com.example.newsfeed.data.remote.dto.ArticleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedRepository( private val savedArticleDao: SavedArticleDao){

    // Lưu bookmark
    suspend fun save(article: ArticleDto) =
        savedArticleDao.insert(article.toSavedArticle())

    // Xóa bookmark
    suspend fun unsave(articleId: Int) =
        savedArticleDao.deleteById(articleId)

    // Kiểm tra đã lưu chưa
    fun isSaved(articleId: Int): Flow<Boolean> =
        savedArticleDao.isSaved(articleId)

    // Lấy bài offline theo id
    suspend fun getOfflineArticle(id: Int): SavedArticle? =
        savedArticleDao.getById(id)

}