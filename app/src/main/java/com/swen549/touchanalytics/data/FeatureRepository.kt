package com.swen549.touchanalytics.data

import com.google.gson.JsonObject
import retrofit2.Response

class FeatureRepository(
    private val firebaseClient: FirebaseClient,
    private val touchalyticsApiService: TouchalyticsApiService
) {
    fun createFeature(userId: Int, feature: Feature) {
        firebaseClient.featuresRef.child(userId.toString()).push().setValue(feature)
    }

    suspend fun authenticateFeature(userId: Int, feature: Feature): Response<JsonObject> = touchalyticsApiService.authenticate(userId, feature)
}