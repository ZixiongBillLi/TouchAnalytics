package com.swen549.touchanalytics.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@SuppressLint("MissingKeepAnnotation")
enum class AppMode {
    ENROLLMENT,
    VERIFICATION
}

sealed class Routes {
    @Serializable
    data object Login

    @Serializable
    data class Home(
        val userId: Int,
        val mode: AppMode
    )

    @Serializable
    data class Chat(
        val userId: Int,
        val recipientId: Int,
        val mode: AppMode
    )
}

@Composable
fun TouchalyticsApp(
    viewModel: TouchalyticsViewModel = viewModel(
        factory = TouchalyticsViewModel.Factory
    )
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login
    ) {
        composable<Routes.Login> {
            LoginScreen(
                onLoginSuccess = { userId ->
                    navController.navigate(
                        Routes.Home(userId, AppMode.ENROLLMENT)
                    )
                }
            )
        }

        composable<Routes.Home> {
            HomeScreen()
        }

        composable<Routes.Chat> {
            ChatScreen()
        }
    }
}
