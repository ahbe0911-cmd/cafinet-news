package com.cafinet.news.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cafinet.news.ui.detail.NewsDetailScreen
import com.cafinet.news.ui.home.HomeScreen
import com.cafinet.news.ui.settings.SettingsScreen
import com.cafinet.news.ui.splash.SplashScreen

@Composable
fun CafinetNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Destination.Splash.route,
    ) {
        composable(
            route = Destination.Splash.route,
            exitTransition = { fadeOut(animationSpec = androidx.compose.animation.core.tween(250)) },
        ) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = Destination.Home.route,
            enterTransition = { fadeIn(animationSpec = androidx.compose.animation.core.tween(250)) },
        ) {
            HomeScreen(
                onNewsClick = { newsId -> navController.navigate(Destination.NewsDetail.createRoute(newsId)) },
                onSettingsClick = { navController.navigate(Destination.Settings.route) },
            )
        }

        composable(
            route = Destination.NewsDetail.route,
            arguments = listOf(navArgument(Destination.NewsDetail.ARG_NEWS_ID) { type = NavType.LongType }),
        ) {
            NewsDetailScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Destination.Settings.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
