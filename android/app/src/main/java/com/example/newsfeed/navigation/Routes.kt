package com.example.newsfeed.navigation

object Routes {
    const val LOGIN ="login"
    const val HOME ="home"
    const val SIGNUP ="signup"

    const val DETAIL = "detail/{articleId}"

    fun getDetailRoute(articleId: Int) = "detail/$articleId"
}