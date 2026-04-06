package com.swen549.touchanalytics.ui

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.swen549.touchanalytics.data.Message
import java.text.SimpleDateFormat
import java.util.Locale

data class ChatPartner(
    val id: Long,
    val name: String,
    val messages: List<Message>,
    val avatarColor: Color
) {
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    val lastMessage: String
        get() = messages.lastOrNull()?.content ?: ""

    val timestamp: String
        get() = messages.lastOrNull()?.let { 
            timeFormatter.format(it.timestamp.toDate()) 
        } ?: ""
}

fun createSampleMessage(
    id: Long,
    senderId: Long,
    recipientId: Long,
    content: String,
    minutesOffset: Long = 0L
): Message {
    val millis = System.currentTimeMillis() - (minutesOffset * 60 * 1000)
    return Message(
        id = id,
        senderId = senderId,
        recipientId = recipientId,
        content = content,
        timestamp = Timestamp(java.util.Date(millis)),
        read = true
    )
}

val sampleChatPartners = listOf(
    ChatPartner(
        1L, "Bill Li", listOf(
            createSampleMessage(101L, 1L, 0L, "Hey, how's the project going?", 60L),
            createSampleMessage(102L, 0L, 1L, "It's going well! I just finished the UI.", 50L),
            createSampleMessage(103L, 1L, 0L, "That's great! Can you show me a screenshot of the chat list screen when you have a chance?", 48L),
            createSampleMessage(104L, 0L, 1L, "Got the UI working. It looks pretty clean now with the new material 3 components and the navigation logic is almost there.", 5L)
        ), Color(0xFF757575)
    ),
    ChatPartner(
        2L, "Howard Kong", listOf(
            createSampleMessage(201L, 2L, 0L, "Database is now working", 30L),
            createSampleMessage(202L, 2L, 0L, "I've finished setting up the Firebase Realtime Database. We can now store and retrieve message data in real-time across all connected clients.", 25L)
        ), Color(0xFFFBC02D)
    ),
    ChatPartner(
        3L, "Kelvin Ng", listOf(
            createSampleMessage(301L, 3L, 0L, "I've updated the ML server. It should be faster now and handle more concurrent requests. I also improved the feature extraction algorithm for better accuracy.", 1440L)
        ), Color(0xFFEF5350)
    ),
    ChatPartner(
        4L, "Alice Smith", listOf(
            createSampleMessage(401L, 4L, 0L, "See you at the meeting. We'll be discussing the project timeline and the upcoming milestones.", 2880L)
        ), Color(0xFF42A5F5)
    ),
    ChatPartner(
        5L, "Bob Johnson", listOf(
            createSampleMessage(501L, 5L, 0L, "Can you review my PR? I've added the authentication logic and some unit tests. I also refactored some of the older code.", 120L)
        ), Color(0xFF66BB6A)
    ),
    ChatPartner(
        6L, "Charlie Brown", listOf(
            createSampleMessage(601L, 6L, 0L, "Thanks for the help! I really appreciate the time you took to explain the complex parts of the architecture.", 180L)
        ), Color(0xFFEC407A)
    ),
    ChatPartner(
        7L, "David Wilson", listOf(
            createSampleMessage(701L, 7L, 0L, "Let's grab lunch later. I found a new place that serves great ramen just around the corner from the office.", 240L)
        ), Color(0xFF26C6DA)
    ),
    ChatPartner(
        8L, "Eve Davis", listOf(
            createSampleMessage(801L, 8L, 0L, "Did you see the latest news about the upcoming Android release? There are some cool new features for Jetpack Compose.", 300L)
        ), Color(0xFFAB47BC)
    ),
    ChatPartner(
        9L, "Frank Miller", listOf(
            createSampleMessage(901L, 9L, 0L, "I'll be a bit late for the standup today. My train was delayed, but I should be there in about 20 minutes.", 360L)
        ), Color(0xFFFF7043)
    ),
    ChatPartner(
        10L, "Grace Lee", listOf(
            createSampleMessage(1001L, 10L, 0L, "Good job on the demo! The client was very impressed with the progress we've made on the real-time analytics dashboard.", 420L)
        ), Color(0xFF26A69A)
    ),
    ChatPartner(
        11L, "Henry Ford", listOf(
            createSampleMessage(1101L, 11L, 0L, "Any updates on the assembly line software? We need to ensure the timing is precise to avoid any bottlenecks.", 480L)
        ), Color(0xFF3F51B5)
    ),
    ChatPartner(
        12L, "Ivy Chen", listOf(
            createSampleMessage(1201L, 12L, 0L, "The design assets are ready for download. I've uploaded the high-res icons and the updated style guide to the shared drive.", 540L)
        ), Color(0xFF8BC34A)
    ),
    ChatPartner(
        13L, "Jack Sparrow", listOf(
            createSampleMessage(1301L, 13L, 0L, "Where is the rum? And more importantly, when are we planning the next major release for the navigation module?", 600L)
        ), Color(0xFF795548)
    ),
    ChatPartner(
        14L, "Kelly Wright", listOf(
            createSampleMessage(1401L, 14L, 0L, "I've finished the documentation for the API. It now includes detailed descriptions for all the new endpoints we added last week.", 660L)
        ), Color(0xFFFFC107)
    ),
    ChatPartner(
        15L, "Liam Neeson", listOf(
            createSampleMessage(1501L, 15L, 0L, "I will find you, and I will help you debug that persistent memory leak in the background service.", 720L)
        ), Color(0xFF607D8B)
    )
)
