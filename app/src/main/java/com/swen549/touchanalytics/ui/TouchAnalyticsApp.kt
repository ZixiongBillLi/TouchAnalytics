package com.swen549.touchanalytics.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TouchAnalyticsApp(
    sharedViewModel: TouchAnalyticsViewModel = viewModel(
        factory = TouchAnalyticsViewModel.Factory
    )
) {
    val navController = rememberNavController()

    // Observer Box for swipe gestures
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        // Observe events in the Initial pass (Top-Down) before child composables
                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                        val change = event.changes.first()
                        val touchSize = event.motionEvent?.size ?: 0f

                        sharedViewModel.handleSwipe(
                            change = change,
                            type = event.type,
                            size = touchSize
                        )
                    }
                }
            }
    ) {
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
}
