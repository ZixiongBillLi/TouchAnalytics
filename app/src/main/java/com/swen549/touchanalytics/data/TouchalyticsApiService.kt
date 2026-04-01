package com.swen549.touchanalytics.data

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TouchalyticsApiService {
    @POST("authenticate/{userID}")
    suspend fun authenticate(
        @Path("userID") userID: Int,
        @Body feature: Feature
    ): Response<JsonObject>
}