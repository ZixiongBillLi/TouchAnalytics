package com.swen549.touchanalytics.data

import com.google.firebase.database.FirebaseDatabase

class FirebaseClient {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val messagesRef = database.getReference("messages")
    val featuresRef = database.getReference("touches")
}