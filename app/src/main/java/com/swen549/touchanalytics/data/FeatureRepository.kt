package com.swen549.touchanalytics.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.gson.JsonObject
import com.swen549.touchanalytics.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import retrofit2.Response

sealed class FeatureType {
    data class Enrollment(val feature: Feature = Feature()) : FeatureType()
    data class Verification(
        val feature: Feature = Feature(),
        val match: Boolean = false
    ) : FeatureType()
}

class FeatureRepository(
    private val firebaseClient: FirebaseClient,
    private val touchalyticsApiService: TouchalyticsApiService
) {
    private val TAG = "FeatureRepository"

    suspend fun saveFeature(userId: Long, feature: FeatureType): Boolean = try {
        withTimeout(Constants.DATABASE_TIMEOUT) {
            val ref = firebaseClient.featuresRef.child(userId.toString())
            
            val dataToSave = when (feature) {
                is FeatureType.Enrollment -> feature.feature
                is FeatureType.Verification -> {
                    mapOf(
                        "userId" to feature.feature.userId,
                        "startX" to feature.feature.startX,
                        "startY" to feature.feature.startY,
                        "stopX" to feature.feature.stopX,
                        "stopY" to feature.feature.stopY,
                        "strokeDuration" to feature.feature.strokeDuration,
                        "midStrokeArea" to feature.feature.midStrokeArea,
                        "midStrokePressure" to feature.feature.midStrokePressure,
                        "directionEndToEnd" to feature.feature.directionEndToEnd,
                        "averageDirection" to feature.feature.averageDirection,
                        "averageVelocity" to feature.feature.averageVelocity,
                        "pairwiseVelocityPercentile" to feature.feature.pairwiseVelocityPercentile,
                        "match" to feature.match
                    )
                }
            }

            ref.push()
                .setValue(dataToSave)
                .await()
                
            Log.d(TAG, "saveFeature(): Feature confirmed on server")
            true
        }
    } catch (e: Exception) {
        Log.e(TAG, "saveFeature(): Failed to sync with server: ${e.message}")
        false
    }

    suspend fun clearVerifications(userId: Long) {// clear match/not match data on logout
        try {
            val ref = firebaseClient.featuresRef.child(userId.toString())
            val snapshot = ref.get().await()
            snapshot.children.forEach { child ->
                if (child.hasChild("match")) { // remove data with "match" to clear verification data
                    child.ref.removeValue().await()
                }
            }
            Log.d(TAG, "clearVerifications(): Previous verifications cleared for user $userId")
        } catch (e: Exception) {
            Log.e(TAG, "clearVerifications(): Error clearing verifications: ${e.message}")
        }
    }

    fun getEnrollmentCount(userId: Long): Flow<Int> = callbackFlow {
        val ref = firebaseClient.featuresRef.child(userId.toString())
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.children.count { !it.hasChild("match") }
                Log.d(TAG, "getEnrollmentCount(): Enrollment count for $userId: $count")
                trySend(count)
            }

            override fun onCancelled(e: DatabaseError) {
                Log.e(TAG, "getEnrollmentCount(): Failed to sync with server: ${e.message}")
                close(e.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun getAllVerifications(userId: Long): Flow<List<FeatureType.Verification>> = callbackFlow {
        val ref = firebaseClient.featuresRef.child(userId.toString())
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { child ->
                    val match = child.child("match").getValue<Boolean>()
                    if (match != null) {
                        val feature = child.getValue<Feature>()
                        if (feature != null) FeatureType.Verification(feature, match) else null
                    } else null
                }
                trySend(list)
            }

            override fun onCancelled(e: DatabaseError) {
                Log.e(TAG, "getVerification(): Failed to sync with server: ${e.message}")
                close(e.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun authenticateFeature(userId: Long, feature: Feature): Response<JsonObject> = touchalyticsApiService.authenticate(userId, feature)
}
