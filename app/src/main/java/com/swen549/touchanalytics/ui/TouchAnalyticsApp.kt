package com.swen549.touchanalytics.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@SuppressLint("MissingKeepAnnotation")
@Serializable
enum class AppMode {
    ENROLLMENT,
    VERIFICATION
}

sealed interface Routes {
    @Serializable
    data object Login : Routes

    @Serializable
    data class Home(
        val userId: Int,
        val mode: AppMode
    ) : Routes

    @Serializable
    data class Chat(
        val userId: Int,
        val partnerId: Int,
        val mode: AppMode
    ) : Routes
}

@Composable
fun TouchAnalyticsApp(
    sharedViewModel: TouchAnalyticsViewModel = viewModel(
        factory = TouchAnalyticsViewModel.Factory
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
                        Routes.Home(
                            userId = userId,
                            mode = AppMode.ENROLLMENT,
                        )
                    )
                },
                sharedViewModel = sharedViewModel
            )
        }

        composable<Routes.Home> { backStackEntry ->
            val homeArgs = backStackEntry.toRoute<Routes.Home>()

            HomeScreen(
                userId = homeArgs.userId,
                mode = homeArgs.mode,
                navigateBack = navController::popBackStack,
                sharedViewModel = sharedViewModel
            )
        }

        composable<Routes.Chat> { backStackEntry ->
            val chatArgs = backStackEntry.toRoute<Routes.Chat>()

            ChatScreen(
                userId = chatArgs.userId,
                partnerId = chatArgs.partnerId,
                mode = chatArgs.mode,
                sharedViewModel = sharedViewModel
            )
        }
    }
}
