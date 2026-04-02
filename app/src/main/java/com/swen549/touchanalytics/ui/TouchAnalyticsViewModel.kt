package com.swen549.touchanalytics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.swen549.touchanalytics.TouchAnalyticsApplication
import com.swen549.touchanalytics.data.FeatureRepository
import com.swen549.touchanalytics.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginStatus {
    object NotLoggedIn : LoginStatus()
    object Loading : LoginStatus()
    data class LoggedIn(val userId: Int) : LoginStatus()
    data class Error(val message: String) : LoginStatus()
}

class TouchAnalyticsViewModel(
    private val userRepository: UserRepository,
    private val featureRepository: FeatureRepository
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TouchAnalyticsApplication)
                TouchAnalyticsViewModel(
                    userRepository = application.userRepository,
                    featureRepository = application.featureRepository
                )
            }
        }
    }

    private var _loginState = MutableStateFlow<LoginStatus>(LoginStatus.NotLoggedIn)
    val loginState = _loginState.asStateFlow()

    fun login(userId: Int) {
        viewModelScope.launch {
            _loginState.value = LoginStatus.Loading
            try {
                val user = userRepository.loginOrRegister(userId)
                _loginState.value = LoginStatus.LoggedIn(user.id)
                _userId.value = user.id
            } catch (e: Exception) {
                _loginState.value = LoginStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        _loginState.value = LoginStatus.NotLoggedIn
        _userId.value = null
        _mode.value = AppMode.ENROLLMENT
        resetStats()
    }

    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    private val _mode = MutableStateFlow(AppMode.ENROLLMENT)
    val mode = _mode.asStateFlow()

    private val _enrollmentCount = MutableStateFlow(0)
    val enrollmentCount = _enrollmentCount.asStateFlow()

    private val _matchCount = MutableStateFlow(0)
    val matchCount = _matchCount.asStateFlow()

    private val _nonmatchCount = MutableStateFlow(0)
    val nonmatchCount = _nonmatchCount.asStateFlow()

    fun setMode(newMode: AppMode) {
        _mode.value = newMode
    }

    fun setEnrollmentCount(value: Int) {
        _enrollmentCount.value = value
    }

    fun setMatchCount(value: Int) {
        _matchCount.value = value
    }

    fun setNonMatchCount(value: Int) {
        _nonmatchCount.value = value
    }

    fun resetStats() {
        _enrollmentCount.value = 0
        _matchCount.value = 0
        _nonmatchCount.value = 0
    }
}