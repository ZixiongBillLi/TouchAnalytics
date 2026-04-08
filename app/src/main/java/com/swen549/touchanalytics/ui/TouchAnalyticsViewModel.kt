package com.swen549.touchanalytics.ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.swen549.touchanalytics.Constants
import com.swen549.touchanalytics.TouchAnalyticsApplication
import com.swen549.touchanalytics.data.Feature
import com.swen549.touchanalytics.data.FeatureRepository
import com.swen549.touchanalytics.data.FeatureType
import com.swen549.touchanalytics.data.UserRepository
import com.swen549.touchanalytics.util.Stroke
import com.swen549.touchanalytics.util.TouchPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

    private val _userId = MutableStateFlow<Int?>(null)

    private val _mode = MutableStateFlow(AppMode.ENROLLMENT)
    val mode = _mode.asStateFlow()

    private val _enrollmentCount = MutableStateFlow(0)
    val enrollmentCount = _enrollmentCount.asStateFlow()

    private val _matchCount = MutableStateFlow(0)
    val matchCount = _matchCount.asStateFlow()

    private val _nonmatchCount = MutableStateFlow(0)
    val nonmatchCount = _nonmatchCount.asStateFlow()

    private var observationJobs: List<Job> = emptyList()

    fun login(userId: Int) {
        viewModelScope.launch {
            _loginState.value = LoginStatus.Loading
            try {
                val user = userRepository.loginOrRegister(userId)
                _userId.value = user.id

                val initialCount = featureRepository.getEnrollmentCount(user.id).first()
                _enrollmentCount.value = initialCount

                _mode.value = if (initialCount < Constants.MIN_STROKE_COUNT) {
                    AppMode.ENROLLMENT
                } else {
                    AppMode.VERIFICATION
                }

                _loginState.value = LoginStatus.LoggedIn(user.id)

                observationJobs.forEach { it.cancel() }
                observationJobs += viewModelScope.launch {
                    featureRepository.getEnrollmentCount(user.id).collect { count ->
                        _enrollmentCount.value = count

                        if (count >= Constants.MIN_STROKE_COUNT) {
                            _mode.value = AppMode.VERIFICATION
                        }
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        observationJobs.forEach { it.cancel() }
        observationJobs = emptyList()
        _loginState.value = LoginStatus.NotLoggedIn
        _userId.value = null
        _mode.value = AppMode.ENROLLMENT
        resetStats()
    }

    fun resetStats() {
        _enrollmentCount.value = 0
        _matchCount.value = 0
        _nonmatchCount.value = 0
    }

    private val _activePoints = MutableStateFlow<List<TouchPoint>>(emptyList())
    private var _strokeStartTime: Long = 0

    fun handleSwipe(change: PointerInputChange, type: PointerEventType, size: Float) {
        val x = change.position.x
        val y = change.position.y
        val timestamp = change.uptimeMillis
        val pressure = change.pressure

        when (type) {
            PointerEventType.Press -> {
                _strokeStartTime = timestamp
                _activePoints.value = listOf(TouchPoint(x, y, timestamp, pressure, size))
            }

            PointerEventType.Move -> {
                _activePoints.value += TouchPoint(x, y, timestamp, pressure, size)
            }

            PointerEventType.Release -> {
                _activePoints.value += TouchPoint(x, y, timestamp, pressure, size)

                if (_activePoints.value.size > 3 && _userId.value != null) {
                    val newStroke = Stroke(
                        userId = _userId.value!!,
                        startTime = _strokeStartTime,
                        endTime = timestamp,
                        points = _activePoints.value
                    )

                    processSwipe(_userId.value!!, newStroke.toFeature())
                }

                _activePoints.value = emptyList()
            }
        }
    }

    fun processSwipe(userId: Int, feature: Feature) {
        viewModelScope.launch {
            try {
                if (_mode.value == AppMode.ENROLLMENT) {
                    featureRepository.saveFeature(userId, FeatureType.Enrollment(feature))
                }

                if (_mode.value == AppMode.VERIFICATION) {
                    val response = featureRepository.authenticateFeature(userId, feature)

                    if (response.isSuccessful && response.body() != null) {
                        val isMatch = response.body()!!.get("match").asBoolean
                        val message = response.body()!!.get("message").asString

                        Log.d("TouchAnalyticsVM", "Auth result: $message (Match: $isMatch)")
                        _matchCount.value += if (isMatch) 1 else 0
                        _nonmatchCount.value += if (!isMatch) 1 else 0
                    } else {
                        Log.e("TouchAnalyticsVM", "Authentication failed: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("TouchAnalyticsVM", "Error processing swipe: ${e.message}")
            }
        }
    }
}
