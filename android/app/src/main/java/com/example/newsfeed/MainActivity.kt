package com.example.newsfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.newsfeed.data.dao.ReadArticleDao
import com.example.newsfeed.data.entity.ReadArticle
import com.example.newsfeed.data.repository.ArticleRepository
import com.example.newsfeed.navigation.NavGraph
import com.example.newsfeed.ui.model.HomeViewModel
import com.example.newsfeed.ui.screen.HomeScreen
import com.example.newsfeed.ui.theme.NewsFeedTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewsFeedTheme {
                // Tạo NavController
                val navController = rememberNavController()

                // Hiển thị NavGraph
                NavGraph(
                    navController = navController
                )
            }
        }
    }
}