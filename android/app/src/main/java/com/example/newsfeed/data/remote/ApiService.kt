package com.example.newsfeed.data.remote

import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.remote.dto.CategoryDto
import com.example.newsfeed.data.remote.dto.SourceDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Lấy ds bài
    @GET("api/articles")
    suspend fun getArticles(
        @Query("category") category: String? = null,
        @Query("sourceId") sourceId: Int?    = null,
    ): List<ArticleDto>

    //Lấy chi tiết 1 bài
    @GET("api/articles/{id}")
    suspend fun getArticleById(
        @Path("id") id: Int
    ): ArticleDto

    //Tìm kiếm bài viết

    // Danh sách chuyên mục
    // GET /api/categories
    @GET("api/categories")
    suspend fun getCategories(): List<CategoryDto>

    // Danh sách nguồn báo
    // GET /api/sources
    @GET("api/sources")
    suspend fun getSources(): List<SourceDto>

    //Refresh nguồn báo
    @POST("api/articles/refresh")
    suspend fun refreshArticles()

}