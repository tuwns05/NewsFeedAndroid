package com.example.newsfeed.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_articles")
data class ReadArticle(
    @PrimaryKey
    val articleId: Int,
    val readAt: Long = System.currentTimeMillis()
)
