package com.cafinet.news.navigation

sealed class Destination(val route: String) {
    data object Splash : Destination("splash")
    data object Home : Destination("home")
    data object Settings : Destination("settings")

    data object NewsDetail : Destination("news_detail/{newsId}") {
        const val ARG_NEWS_ID = "newsId"
        fun createRoute(newsId: Long) = "news_detail/$newsId"
    }
}
