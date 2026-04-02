package com.swen549.touchanalytics.data

import android.util.Log
import com.google.gson.JsonObject
import com.swen549.touchanalytics.Constants
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import retrofit2.Response

class FeatureRepository(
    private val firebaseClient: FirebaseClient,
    private val touchalyticsApiService: TouchalyticsApiService
) {
    private val TAG = "FeatureRepository"

    suspend fun saveFeature(userId: Int, feature: Feature): Boolean = try {
        withTimeout(Constants.DATABASE_TIMEOUT) {
            firebaseClient.featuresRef.child(userId.toString()).child("features").push().setValue(feature).await()
            Log.d(TAG, "saveFeature(): Feature confirmed on server")
            true
        }
    } catch (e: Exception) {
        Log.e(TAG, "saveFeature(): Failed to sync with server: ${e.message}")
        false
    }

    suspend fun authenticateFeature(userId: Int, feature: Feature): Response<JsonObject> = touchalyticsApiService.authenticate(userId, feature)
}