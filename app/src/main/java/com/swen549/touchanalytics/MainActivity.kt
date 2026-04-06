package com.swen549.touchanalytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.swen549.touchanalytics.ui.ChatListScreen
import com.swen549.touchanalytics.ui.ChatScreen
import com.swen549.touchanalytics.ui.LoginScreen
import com.swen549.touchanalytics.ui.TouchViewModel
import com.swen549.touchanalytics.ui.theme.TouchAnalyticsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouchAnalyticsTheme {
                TouchAnalyticsApp()
            }
        }
    }
}

@Composable
fun TouchAnalyticsApp() {
    val navController = rememberNavController()
    val viewModel: TouchViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = { userId ->
                viewModel.login(userId)
                navController.navigate("chatList")
            })
        }
        composable("chatList") {
            ChatListScreen(
                viewModel = viewModel,
                onPartnerClick = { partnerId ->
                    navController.navigate("chat/$partnerId")
                }
            )
        }
        composable(
            route = "chat/{partnerId}",
            arguments = listOf(navArgument("partnerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val partnerId = backStackEntry.arguments?.getInt("partnerId") ?: 0
            ChatScreen(
                partnerId = partnerId,
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}
