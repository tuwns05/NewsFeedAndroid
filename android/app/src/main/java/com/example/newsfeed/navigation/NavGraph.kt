package com.example.newsfeed.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsfeed.data.repository.AuthRepository
import com.example.newsfeed.ui.screen.HomeScreen
import com.example.newsfeed.ui.screen.MainScreen
import com.example.newsfeed.ui.screen.authen.LoginScreen
import com.example.newsfeed.ui.screen.authen.SignUpScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val repo = AuthRepository()

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
                    // Khi nhấn vào bài báo
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


    }
}