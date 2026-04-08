package com.swen549.touchanalytics.data

class UserRepository {
    fun loginOrRegister(userId: Long): User {
        return User(userId)
    }
}
