package com.swen549.touchanalytics.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseClient {
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    
    val usersRef: DatabaseReference by lazy { database.reference }
    val featuresRef: DatabaseReference by lazy { database.reference }
}
