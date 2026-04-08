package com.swen549.touchanalytics.data

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class Message(
    val id: Long,
    val senderId: Long,
    val recipientId: Long,
    val content: String,
    val timestamp: Timestamp,
    val read: Boolean
) {
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    val timestampString: String
        get() = timeFormatter.format(timestamp.toDate())
}