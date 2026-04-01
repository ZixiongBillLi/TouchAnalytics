package com.swen549.touchanalytics.data

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firebaseClient: FirebaseClient
) {
    fun createUser(user: User) {
        firebaseClient.usersRef.child(user.id.toString()).setValue(user)
    }

    suspend fun getUser(userId: String): User? {
        val snapshot = firebaseClient.usersRef.child(userId).get().await()
        return snapshot.getValue(User::class.java)
    }
}
