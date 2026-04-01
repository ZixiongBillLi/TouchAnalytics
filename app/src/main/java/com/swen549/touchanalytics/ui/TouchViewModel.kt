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

    var enrollmentCount by mutableStateOf(0)
        private set

    var lastResult by mutableStateOf<String?>(null)
        private set

    var matchCount by mutableStateOf(0)
        private set

    var mismatchCount by mutableStateOf(0)
        private set

    fun login(id: Int) {
        userID = id
    }
}
