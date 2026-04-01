package com.swen549.touchanalytics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.swen549.touchanalytics.ui.theme.TouchAnalyticsTheme

@Composable
fun TouchalyticsApp(
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
