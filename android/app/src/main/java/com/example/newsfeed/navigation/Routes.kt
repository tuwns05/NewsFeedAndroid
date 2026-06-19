package com.example.newsfeed.navigation

object Routes {
    const val LOGIN ="login"
    const val HOME ="home"
    const val SIGNUP ="signup"

    const val DETAIL = "detail/{articleId}"

    const val DETAIL_OFFLINE = "detail_offline/{articleId}"
    const val SAVED = "saved"

    fun getDetailRoute(articleId: Int) = "detail/$articleId"
    fun getDetailOfflineRoute(articleId: Int) = "detail_offline/$articleId"
}