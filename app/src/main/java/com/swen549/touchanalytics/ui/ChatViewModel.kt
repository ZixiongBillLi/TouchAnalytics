package com.swen549.touchanalytics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.swen549.touchanalytics.TouchAnalyticsApplication
import com.swen549.touchanalytics.data.ChatPartner
import com.swen549.touchanalytics.data.Message
import com.swen549.touchanalytics.data.MessageRepository
import com.swen549.touchanalytics.data.User
import com.swen549.touchanalytics.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
): ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TouchAnalyticsApplication)
                ChatViewModel(
                    userRepository = application.userRepository,
                    messageRepository = application.messageRepository
                )
            }
        }
    }

    private val _partner = MutableStateFlow<ChatPartner?>(null)
    val partner = _partner.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun startListening(userId: Long, partnerId: Long) {
        viewModelScope.launch {
            _partner.value = messageRepository.getChatPartner(partnerId)
            _messages.value = messageRepository.getMessages(partnerId)
        }
    }

    private val _input = MutableStateFlow<String>("")
    val input = _input.asStateFlow()

    fun setInput(value: String) {
        _input.value = value
    }

    private var _showMoreMenu = MutableStateFlow(false)
    val showMoreMenu = _showMoreMenu.asStateFlow()

    fun setShowMoreMenu(value: Boolean) {
        _showMoreMenu.value = value
    }
}