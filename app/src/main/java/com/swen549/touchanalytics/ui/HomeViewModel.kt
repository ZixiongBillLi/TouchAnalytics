package com.swen549.touchanalytics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.swen549.touchanalytics.TouchAnalyticsApplication
import com.swen549.touchanalytics.data.ChatPartner
import com.swen549.touchanalytics.data.MessageRepository
import com.swen549.touchanalytics.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
): ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TouchAnalyticsApplication)
                HomeViewModel(
                    userRepository = application.userRepository,
                    messageRepository = application.messageRepository
                )
            }
        }
    }

    private val _chatPartners = MutableStateFlow<List<ChatPartner>>(emptyList())
    val chatPartners = _chatPartners.asStateFlow()

    fun startListening(userId: Long) {
        viewModelScope.launch {
            _chatPartners.value = messageRepository.getChatPartners(userId)
        }
    }

    private var _searchActive = MutableStateFlow(false)
    val searchActive = _searchActive.asStateFlow()

    fun setSearchActive(value: Boolean) {
        _searchActive.value = value
    }

    private var _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    fun setQuery(value: String) {
        _query.value = value
    }

    private var _showMoreMenu = MutableStateFlow(false)
    val showMoreMenu = _showMoreMenu.asStateFlow()

    fun setShowMoreMenu(value: Boolean) {
        _showMoreMenu.value = value
    }
}