package com.swen549.touchanalytics.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    onLoginSuccess: (Int) -> Unit,
    sharedViewModel: TouchAnalyticsViewModel,
) {
    val loginState by sharedViewModel.loginState.collectAsStateWithLifecycle()

    var userIdText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    // Handle login state changes
    LaunchedEffect(loginState) {
        Log.d("LoginScreen", "Login state changed: $loginState")
        when (val state = loginState) {
            is LoginStatus.LoggedIn -> {
                onLoginSuccess(state.userId)
            }
            is LoginStatus.Error -> {
                errorText = state.message
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stroke Authentication",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your User ID to proceed",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = userIdText,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    userIdText = input
                    errorText = null
                }
            },
            label = { Text("User ID") },
            placeholder = { Text("Enter your ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorText != null,
            enabled = loginState !is LoginStatus.Loading
        )

        if (errorText != null) {
            Text(
                text = errorText!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (loginState is LoginStatus.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (userIdText.length >= 3) {
                        sharedViewModel.login(userIdText.toInt())
                    } else {
                        errorText = "User ID must be at least 3 digits"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Login")
            }
        }
    }
}
