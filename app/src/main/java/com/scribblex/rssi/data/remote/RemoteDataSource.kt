package com.scribblex.rssi.data.remote

import com.scribblex.rssi.data.entities.Rssi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun sendRssiData(rssiPayload: List<Rssi>) {
        apiService.sendRssiData(rssiPayload)
    }
}