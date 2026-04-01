package com.swen549.touchanalytics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.swen549.touchanalytics.TouchalyticsApplication
import com.swen549.touchanalytics.data.MessageRepository
import com.swen549.touchanalytics.data.FeatureRepository
import com.swen549.touchanalytics.data.UserRepository

class TouchalyticsViewModel(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val featureRepository: FeatureRepository
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TouchalyticsApplication)
                TouchalyticsViewModel(
                    userRepository = application.userRepository,
                    messageRepository = application.messageRepository,
                    featureRepository = application.featureRepository
                )
            }
        }
    }
}