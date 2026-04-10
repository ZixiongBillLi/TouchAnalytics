package com.swen549.touchanalytics.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.swen549.touchanalytics.Constants

class FirebaseClient {
    private val database: FirebaseDatabase by lazy { 
        FirebaseDatabase.getInstance(Constants.FIREBASE_URL) 
    }
    
    val usersRef: DatabaseReference by lazy { database.reference }
    val messagesRef: DatabaseReference by lazy { database.getReference("messages") }
    val featuresRef: DatabaseReference by lazy { database.reference }
}
