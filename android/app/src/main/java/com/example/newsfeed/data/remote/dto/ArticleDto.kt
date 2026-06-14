package com.example.newsfeed.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticleDto (

    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    val link: String,
    val publishedAt: String,
    val sourceName: String,
    val category: String
)