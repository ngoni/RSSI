package com.scribblex.rssi.data.repository

import androidx.lifecycle.LiveData
import com.scribblex.rssi.data.entities.Rssi
import com.scribblex.rssi.data.local.RssiDataSource
import javax.inject.Inject

class RssiRepository @Inject constructor(
    private val rssiDataSource: RssiDataSource
) {

    fun initWirelessScan() = rssiDataSource.initWirelessScan()

    val wirelessScanResults: LiveData<List<Rssi>> = rssiDataSource.wirelessScanResults

}