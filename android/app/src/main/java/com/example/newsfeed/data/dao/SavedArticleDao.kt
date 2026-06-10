package com.example.newsfeed.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsfeed.data.entity.SavedArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {

    //Lấy tất cả các bài đã lưu sắp lên treen cùng
    @Query("Select * from saved_articles order by savedAt DESC")
    fun getAll(): Flow<List<SavedArticle>>

    //Lưu bài vào bookmark(đánh dấu)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: SavedArticle)

    //Xóa bookmark
    @Delete
    suspend fun delete(article: SavedArticle)

    // Xóa theo id
    @Query("DELETE FROM saved_articles WHERE id = :id")
    suspend fun deleteById(id: Int)

    // Kiểm tra bài đã bookmark chưa — dùng cho icon bookmark
    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE id = :id)")
    fun isSaved(id: Int): Flow<Boolean>

    // Lấy 1 bài theo id — dùng khi offline
    @Query("SELECT * FROM saved_articles WHERE id = :id")
    suspend fun getById(id: Int): SavedArticle?
}