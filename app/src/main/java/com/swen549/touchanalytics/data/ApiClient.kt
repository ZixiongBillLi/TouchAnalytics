package com.swen549.touchanalytics.data

import com.swen549.touchanalytics.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val BASE_URL = BuildConfig.SERVER_URL

    val touchalyticsApiService: TouchalyticsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TouchalyticsApiService::class.java)
    }
}