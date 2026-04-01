package com.swen549.touchanalytics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TouchAnalyticsApp(
    viewModel: TouchalyticsViewModel = viewModel(
        factory = TouchalyticsViewModel.Factory
    )
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "Touchalytics!",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
