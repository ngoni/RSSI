package com.scribblex.rssi.data.remote

import com.scribblex.rssi.data.entities.RssiPayload
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/")
    suspend fun sendRssiData(@Body rssiPayload: RssiPayload)
}