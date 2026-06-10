package com.example.newsfeed.data.repository

import com.example.newsfeed.data.dao.ReadArticleDao
import com.example.newsfeed.data.entity.ReadArticle
import com.example.newsfeed.data.remote.ApiService
import com.example.newsfeed.data.remote.RetrofitClient
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.remote.dto.CategoryDto
import com.example.newsfeed.data.remote.dto.SourceDto
import kotlinx.coroutines.flow.Flow
import javax.xml.transform.Source

class ArticleRepository(

    ){
    private val apiService = RetrofitClient.retrofit
    //Lấy danh sách bài viết từ api
    suspend fun getArticles(
        category: String? = null,
        sourceId: Int? = null,
        page: Int = 1
    ): Result<List<ArticleDto>> = try {
        val articles = apiService.getArticles(
            category = category,
            sourceId = sourceId,
            page     = page
        )
        Result.success(articles)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getCategories(): Result<List<CategoryDto>> = try {
        Result.success(apiService.getCategories())
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Danh sách nguồn báo
    suspend fun getSources(): Result<List<SourceDto>> = try {
        Result.success(apiService.getSources())
    } catch (e: Exception) {
        Result.failure(e)
    }


}
