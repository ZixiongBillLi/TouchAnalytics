package com.swen549.touchanalytics.data

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class ChatPartner(
    val id: Long,
    val name: String,
    val avatarColor: Color,
    val messages: List<Message>
) {
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    val lastMessage: String
        get() = messages.lastOrNull()?.content ?: ""

    val lastMessageTimestamp: String
        get() = messages.lastOrNull()?.let {
            timeFormatter.format(it.timestamp.toDate())
        } ?: ""
}

fun createSampleMessage(
    id: Long,
    senderId: Long,
    recipientId: Long,
    content: String,
    minutesOffset: Long = 0
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
        id = 1L,
        name = "Bill Li",
        avatarColor = Color(0xFF757575),
        messages = listOf(
            createSampleMessage(101, 1, 0, "Hey, how's the project going?", 60),
            createSampleMessage(102, 0, 1, "It's going well! I just finished the UI.", 50),
            createSampleMessage(103, 1, 0, "That's great! Can you show me a screenshot of the chat list screen when you have a chance?", 48),
            createSampleMessage(104, 0, 1, "Got the UI working. It looks pretty clean now with the new material 3 components and the navigation logic is almost there.", 5)
        )
    ),
    ChatPartner(
        id = 2L,
        name = "Howard Kong",
        avatarColor = Color(0xFFFBC02D),
        messages = listOf(
            createSampleMessage(201, 2, 0, "Database is now working", 30),
            createSampleMessage(202, 2, 0, "I've finished setting up the Firebase Realtime Database. We can now store and retrieve message data in real-time across all connected clients.", 25)
        )
    ),
    ChatPartner(
        id = 3L,
        name = "Kelvin Ng",
        avatarColor = Color(0xFFEF5350),
        messages = listOf(
            createSampleMessage(301, 3, 0, "I've updated the ML server. It should be faster now and handle more concurrent requests. I also improved the feature extraction algorithm for better accuracy.", 1440)
        )
    ),
    ChatPartner(
        id = 4L,
        name = "Alice Smith",
        avatarColor = Color(0xFF42A5F5),
        messages = listOf(
            createSampleMessage(401, 4, 0, "See you at the meeting. We'll be discussing the project timeline and the upcoming milestones.", 2880)
        )
    ),
    ChatPartner(
        id = 5L,
        name = "Bob Johnson",
        avatarColor = Color(0xFF66BB6A),
        messages = listOf(
            createSampleMessage(501, 5, 0, "Can you review my PR? I've added the authentication logic and some unit tests. I also refactored some of the older code.", 120)
        )
    ),
    ChatPartner(
        id = 6L,
        name = "Charlie Brown",
        avatarColor = Color(0xFFEC407A),
        messages = listOf(
            createSampleMessage(601, 6, 0, "Thanks for the help! I really appreciate the time you took to explain the complex parts of the architecture.", 180)
        )
    ),
    ChatPartner(
        id = 7L,
        name = "David Wilson",
        avatarColor = Color(0xFF26C6DA),
        messages = listOf(
            createSampleMessage(701, 7, 0, "Let's grab lunch later. I found a new place that serves great ramen just around the corner from the office.", 240)
        )
    ),
    ChatPartner(
        id = 8L,
        name = "Eve Davis",
        avatarColor = Color(0xFFAB47BC),
        messages = listOf(
            createSampleMessage(801, 8, 0, "Did you see the latest news about the upcoming Android release? There are some cool new features for Jetpack Compose.", 300)
        )
    ),
    ChatPartner(
        id = 9L,
        name = "Frank Miller",
        avatarColor = Color(0xFFFF7043),
        messages = listOf(
            createSampleMessage(901, 9, 0, "I'll be a bit late for the standup today. My train was delayed, but I should be there in about 20 minutes.", 360)
        )
    ),
    ChatPartner(
        id = 10L,
        name = "Grace Lee",
        avatarColor = Color(0xFF26A69A),
        messages = listOf(
            createSampleMessage(1001, 10, 0, "Good job on the demo! The client was very impressed with the progress we've made on the real-time analytics dashboard.", 420)
        )
    ),
    ChatPartner(
        id = 11L,
        name = "Henry Ford",
        avatarColor = Color(0xFF3F51B5),
        messages = listOf(
            createSampleMessage(1101, 11, 0, "Any updates on the assembly line software? We need to ensure the timing is precise to avoid any bottlenecks.", 480)
        )
    ),
    ChatPartner(
        id = 12L,
        name = "Ivy Chen",
        avatarColor = Color(0xFF8BC34A),
        messages = listOf(
            createSampleMessage(1201, 12, 0, "The design assets are ready for download. I've uploaded the high-res icons and the updated style guide to the shared drive.", 540)
        )
    ),
    ChatPartner(
        id = 13L,
        name = "Jack Sparrow",
        avatarColor = Color(0xFF795548),
        messages = listOf(
            createSampleMessage(1301, 13, 0, "Where is the rum? And more importantly, when are we planning the next major release for the navigation module?", 600)
        )
    ),
    ChatPartner(
        id = 14L,
        name = "Kelly Wright",
        avatarColor = Color(0xFFFFC107),
        messages = listOf(
            createSampleMessage(1401, 14, 0, "I've finished the documentation for the API. It now includes detailed descriptions for all the new endpoints we added last week.", 660)
        )
    ),
    ChatPartner(
        id = 15L,
        name = "Liam Neeson",
        avatarColor = Color(0xFF607D8B),
        messages = listOf(
            createSampleMessage(1501, 15, 0, "I will find you, and I will help you debug that persistent memory leak in the background service.", 720)
        )
    )
)
