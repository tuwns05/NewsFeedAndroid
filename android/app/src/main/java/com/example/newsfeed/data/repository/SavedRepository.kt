package com.example.newsfeed.data.repository


import android.content.Context
import com.example.newsfeed.data.database.AppDatabase
import com.example.newsfeed.data.entity.SavedArticle
import com.example.newsfeed.data.entity.toSavedArticle
import com.example.newsfeed.data.remote.dto.ArticleDto
import kotlinx.coroutines.flow.Flow
import com.example.newsfeed.data.dao.SavedArticleDao


class SavedRepository(context: Context){
    private val savedArticleDao: SavedArticleDao =
        AppDatabase.getDatabase(context).savedArticleDao()

    // Lưu bookmark
    suspend fun save(article: ArticleDto, content: String, userId: String) =
        savedArticleDao.insert(article.toSavedArticle(content, userId))

    // Xóa bookmark
    suspend fun unsave(articleId: Int,userId: String) =
        savedArticleDao.deleteById(articleId,userId  )

    // Kiểm tra đã lưu chưa
    fun isSaved(articleId: Int,userId: String): Flow<Boolean> =
        savedArticleDao.isSaved(articleId, userId)

    // Lấy bài offline theo id
    suspend fun getOfflineArticle(id: Int,userId: String): SavedArticle? =
        savedArticleDao.getById(id,userId)

    // lấy tất cả bài of
    fun getSavedArticles(userId :String): Flow<List<SavedArticle>> =
        savedArticleDao.getAll(userId)
}