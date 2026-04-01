package com.swen549.touchanalytics

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.swen549.touchanalytics.data.ApiClient
import com.swen549.touchanalytics.data.MessageRepository
import com.swen549.touchanalytics.data.FeatureRepository
import com.swen549.touchanalytics.data.FirebaseClient
import com.swen549.touchanalytics.data.UserRepository

class TouchalyticsApplication: Application() {
    lateinit var userRepository: UserRepository
    lateinit var messageRepository: MessageRepository
    lateinit var featureRepository: FeatureRepository

    override fun onCreate() {
        super.onCreate()
        val client = FirebaseClient()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        userRepository = UserRepository(client)
        messageRepository = MessageRepository(client)
        featureRepository = FeatureRepository(client, ApiClient.touchalyticsApiService)
    }
}
