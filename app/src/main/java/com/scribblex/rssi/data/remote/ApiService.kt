package com.scribblex.rssi.data.remote

import com.scribblex.rssi.data.entities.Rssi
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("post")
    suspend fun sendRssiData(@Body rssiPayload: List<Rssi>)
}