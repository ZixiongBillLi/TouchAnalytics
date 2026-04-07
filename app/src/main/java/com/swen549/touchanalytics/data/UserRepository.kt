package com.swen549.touchanalytics.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.swen549.touchanalytics.Constants
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class UserRepository(
    private val firebaseClient: FirebaseClient
) {
    private val TAG = "UserRepository"

    suspend fun createUser(user: User): Boolean = try {
        withTimeout(Constants.DATABASE_TIMEOUT) {
            firebaseClient.usersRef.child(user.id.toString()).setValue(user).await()
            Log.d(TAG, "createUser(): User ${user.id} confirmed on server")
            true
        }
    } catch (e: Exception) {
        Log.e(TAG, "createUser(): Failed to sync with server: ${e.message}")
        false
    }

    suspend fun getUser(userId: Long): User? = withTimeoutOrNull(Constants.DATABASE_TIMEOUT) {
        suspendCancellableCoroutine { continuation ->
            val ref = firebaseClient.usersRef.child(userId.toString())
            
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue<User>()
                    Log.d(TAG, "getUser(): Data received for $userId. Exists: ${snapshot.exists()}")
                    if (continuation.isActive) continuation.resume(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "getUser(): Firebase error: ${error.message}")
                    if (continuation.isActive) continuation.resume(null)
                }
            }

            ref.addListenerForSingleValueEvent(listener)
            
            // Clean up listener if the coroutine is canceled
            continuation.invokeOnCancellation {
                ref.removeEventListener(listener)
            }
        }
    }

    suspend fun loginOrRegister(userId: Long): User {
        val existingUser = getUser(userId)
        if (existingUser != null) {
            return existingUser
        }

        Log.d(TAG, "loginOrRegister(): Creating new user...")
        val newUser = User(userId)
        val success = createUser(newUser)
        
        if (!success) {
            throw Exception("Could not reach Firebase server to create account.")
        }

        return newUser
    }
}
