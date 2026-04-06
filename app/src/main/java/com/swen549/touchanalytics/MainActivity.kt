package com.swen549.touchanalytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.swen549.touchanalytics.ui.ChatListScreen
import com.swen549.touchanalytics.ui.LoginScreen
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

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = { userId ->
                navController.navigate("chatList/$userId")
            })
        }
        composable(
            route = "chatList/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            ChatListScreen(userId = userId)
        }
    }
}
