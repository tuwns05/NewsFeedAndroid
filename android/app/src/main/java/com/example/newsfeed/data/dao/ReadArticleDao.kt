package com.example.newsfeed.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsfeed.data.entity.ReadArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadArticleDao {
    // Đánh dấu đã đọc — bỏ qua nếu đã có
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun markAsRead(article: ReadArticle)

    // Lấy tất cả ID bài đã đọc — dùng để làm mờ card
    @Query("SELECT articleId FROM read_articles")
    fun getAllReadIds(): Flow<List<Int>>

    // Kiểm tra bài đã đọc chưa
    @Query("SELECT EXISTS(SELECT 1 FROM read_articles WHERE articleId = :id)")
    suspend fun isRead(id: Int): Boolean

    // Xóa lịch sử đọc — dùng trong Cài đặt
    @Query("DELETE FROM read_articles")
    suspend fun clearAll()
}