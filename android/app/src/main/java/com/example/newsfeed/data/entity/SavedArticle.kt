package com.example.newsfeed.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsfeed.data.remote.dto.ArticleDto


@Entity(tableName = "saved_articles")
data class SavedArticle(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val link: String,
    val sourceName: String,
    val category: String,
    val publishedAt: String,
    val savedAt: Long = System.currentTimeMillis(),
    val content : String
)

fun ArticleDto.toSavedArticle(
    content: String
) = SavedArticle(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    link = link,
    sourceName = sourceName,
    category = category,
    publishedAt = publishedAt,
    content = content
)

// Chuyển ngược SavedArticle → ArticleDto để dùng lại ArticleCard
fun SavedArticle.toArticleDto() = ArticleDto(
    id          = id,
    title       = title,
    description = description,
    imageUrl    = imageUrl,
    link        = link,
    sourceName  = sourceName,
    category    = category,
    publishedAt = publishedAt
)