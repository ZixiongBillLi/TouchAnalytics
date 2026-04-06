package com.swen549.touchanalytics.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class AppMode {
    ENROLLMENT,
    VERIFICATION
}

class TouchViewModel : ViewModel() {

    var userID by mutableStateOf<Int?>(null)
        private set

    var mode by mutableStateOf(AppMode.ENROLLMENT)
        private set

    var enrollmentCount by mutableStateOf(20) // Sample data
        private set
    
    val maxEnrollment = 50

    var matchCount by mutableStateOf(14) // Sample data
        private set

    var mismatchCount by mutableStateOf(5) // Sample data
        private set

    fun login(id: Int) {
        userID = id
    }

    fun toggleMode() {
        mode = if (mode == AppMode.ENROLLMENT) AppMode.VERIFICATION else AppMode.ENROLLMENT
    }
}
