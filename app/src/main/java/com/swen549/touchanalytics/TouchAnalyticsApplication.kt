package com.swen549.touchanalytics

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.swen549.touchanalytics.data.ApiClient
import com.swen549.touchanalytics.data.MessageRepository
import com.swen549.touchanalytics.data.FeatureRepository
import com.swen549.touchanalytics.data.FirebaseClient
import com.swen549.touchanalytics.data.UserRepository

class TouchAnalyticsApplication: Application() {
    lateinit var userRepository: UserRepository
    lateinit var messageRepository: MessageRepository
    lateinit var featureRepository: FeatureRepository

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        val client = FirebaseClient()
        userRepository = UserRepository(client)
        messageRepository = MessageRepository(client)
        featureRepository = FeatureRepository(client, ApiClient.touchalyticsApiService)
    }
}
