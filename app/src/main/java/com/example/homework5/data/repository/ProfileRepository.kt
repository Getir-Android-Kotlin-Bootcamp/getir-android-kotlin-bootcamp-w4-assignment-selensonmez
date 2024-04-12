package com.example.homework5.data.repository

import com.example.homework5.network.RemoteApi
import com.example.homework5.data.model.User

class ProfileRepository(private val remoteApi: RemoteApi) {
    fun getProfile(userId: String, callback: (User?, String?) -> Unit) {
        remoteApi.getProfile(userId, callback)
    }
}

