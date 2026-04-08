package com.swen549.touchanalytics.data

class MessageRepository(
    private val firebaseClient: FirebaseClient
) {
    /**
     * TODO:
     * move chat partner and profile settings to users and move getChatPartner to users repo
     * implement messages with firebase
     */
    suspend fun getChatPartner(userId: Long): ChatPartner? {
        return sampleChatPartners.find { it.id == userId }
    }

    suspend fun getChatPartners(userId: Long): List<ChatPartner> {
        return sampleChatPartners
    }

    suspend fun getMessages(partnerId: Long): List<Message> {
        return sampleChatPartners.find { it.id == partnerId }?.messages ?: emptyList()
    }
}