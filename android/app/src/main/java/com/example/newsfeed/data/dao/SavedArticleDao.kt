package com.example.newsfeed.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsfeed.data.entity.SavedArticle
import kotlinx.coroutines.flow.Flow


// data/dao/SavedArticleDao.kt
@Dao
interface SavedArticleDao {

    // Lấy tất cả bài đã lưu của user
    @Query("SELECT * FROM saved_articles WHERE userId = :userId ORDER BY savedAt DESC")
    fun getAll(userId: String): Flow<List<SavedArticle>>

    // Lưu bài vào bookmark
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: SavedArticle)

    //  Xóa bookmark
    @Query("DELETE FROM saved_articles WHERE id = :id AND userId = :userId")
    suspend fun deleteById(id: Int, userId: String)

    // Kiểm tra đã bookmark chưa
    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE id = :id AND userId = :userId)")
    fun isSaved(id: Int, userId: String): Flow<Boolean>

    //Lấy 1 bài theo id và userId
    @Query("SELECT * FROM saved_articles WHERE id = :id AND userId = :userId")
    suspend fun getById(id: Int, userId: String): SavedArticle?
}