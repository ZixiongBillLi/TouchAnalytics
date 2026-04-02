package com.swen549.touchanalytics.ui

import androidx.compose.ui.graphics.Color

data class ChatPartner(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val avatarColor: Color
)

val sampleChatPartners = listOf(
    ChatPartner(1, "Bill Li", "How's the project going?", "10:15 AM", Color(0xFF757575)),
    ChatPartner(2, "Howard Kong", "I've updated the ML server.", "9:45 AM", Color(0xFFFBC02D)),
    ChatPartner(3, "Kelvin Ng", "The UI looks great!", "Yesterday", Color(0xFFEF5350)),
    ChatPartner(4, "Alice Smith", "See you at the meeting.", "Monday", Color(0xFF42A5F5)),
    ChatPartner(5, "Bob Johnson", "Can you review my PR?", "10:30 AM", Color(0xFF66BB6A)),
    ChatPartner(6, "Charlie Brown", "Thanks for the help!", "11:00 AM", Color(0xFFEC407A)),
    ChatPartner(7, "David Wilson", "Let's grab lunch later.", "12:15 PM", Color(0xFF26C6DA)),
    ChatPartner(8, "Eve Davis", "Did you see the latest news?", "1:30 PM", Color(0xFFAB47BC)),
    ChatPartner(9, "Frank Miller", "I'll be a bit late.", "2:45 PM", Color(0xFFFF7043)),
    ChatPartner(10, "Grace Lee", "Good job on the demo!", "4:00 PM", Color(0xFF26A69A))
)
