package com.scribblex.rssi.data.repository

import com.scribblex.rssi.data.entities.Rssi
import com.scribblex.rssi.data.remote.RemoteDataSource
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun sendRssiData(rssiPayload: List<Rssi>) {
        remoteDataSource.sendRssiData(rssiPayload)
    }
}