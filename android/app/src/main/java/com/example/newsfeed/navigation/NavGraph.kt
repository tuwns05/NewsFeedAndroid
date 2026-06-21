package com.example.newsfeed.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsfeed.data.database.AppDatabase
import com.example.newsfeed.data.entity.SavedArticle
import com.example.newsfeed.data.entity.toArticleDto
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.data.repository.AuthRepository
import com.example.newsfeed.data.repository.SavedRepository
import com.example.newsfeed.ui.model.HomeViewModel
import com.example.newsfeed.ui.screen.DetailScreen
import com.example.newsfeed.ui.screen.HomeScreen
import com.example.newsfeed.ui.screen.MainScreen
import com.example.newsfeed.ui.screen.SavedScreen
import com.example.newsfeed.ui.screen.authen.LoginScreen
import com.example.newsfeed.ui.screen.authen.SignUpScreen

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavGraph(navController: NavHostController) {
    val repo = AuthRepository(context = LocalContext.current)

    val startDestination =
        if(repo.isLoggedIn())
            Routes.HOME
        else
            Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Routes.LOGIN) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SIGNUP)
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignUpScreen(
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.HOME) {
            MainScreen(
                onArticleClick = { article ->
                    navController.navigate(Routes.getDetailRoute(article.id))
                },
                onArticleClick2 = { article ->
                    navController.navigate(Routes.getDetailOfflineRoute(article.id))
                },
                onLogout = {
                    repo.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        // Màn hình saved
        composable(Routes.SAVED) {
            SavedScreen(
                onArticleClick = { article ->
                    navController.navigate(Routes.getDetailRoute(article.id))
                },
                onBack = { navController.popBackStack() }
            )
        }
        // Route offline — lấy từ Room
        composable(
            route = Routes.DETAIL_OFFLINE ,
            arguments = listOf(
                navArgument("articleId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId")
            var savedArticle by remember { mutableStateOf<SavedArticle?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            val context = LocalContext.current

            LaunchedEffect(articleId) {
                if (articleId != null) {
                    val repository = SavedRepository(context)
                    savedArticle = repository.getOfflineArticle(articleId,repo.getCurrentUserId() ?: "")
                    isLoading = false
                }
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                DetailScreen(
                    article = savedArticle?.toArticleDto(),
                    offlineContent = savedArticle?.content,
                    onBack = { navController.popBackStack() }
                )
            }
        }
            // Màn hình detail với argument (Int)
        composable(
                route = Routes.DETAIL,
                arguments = listOf(
                    navArgument("articleId") {
                        type = NavType.IntType  // Chuyển sang IntType
                    }
                )
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getInt("articleId")
                var article by remember { mutableStateOf<ArticleDto?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                val homeViewModel = HomeViewModel()
                // Load article khi có ID
                LaunchedEffect(articleId) {
                    if (articleId != null) {
                        isLoading = true
                        article = homeViewModel.getArticleById(articleId)
                        isLoading = false
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetailScreen(
                        article = article,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
}



