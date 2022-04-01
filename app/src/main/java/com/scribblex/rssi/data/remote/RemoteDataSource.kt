package com.scribblex.rssi.data.remote

import com.scribblex.rssi.data.entities.RssiPayload
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun sendRssiData(rssiPayload: RssiPayload) {
        apiService.sendRssiData(rssiPayload)
    }
}