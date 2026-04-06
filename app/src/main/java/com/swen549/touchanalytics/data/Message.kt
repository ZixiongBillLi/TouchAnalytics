package com.swen549.touchanalytics.data

import com.google.firebase.Timestamp

data class Message(
    val id: Long,
    val senderId: Long,
    val recipientId: Long,
    val content: String,
    val timestamp: Timestamp,
    val read: Boolean
)
