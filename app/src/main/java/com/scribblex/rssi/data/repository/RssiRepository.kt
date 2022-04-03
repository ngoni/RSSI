package com.scribblex.rssi.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.scribblex.rssi.data.entities.Rssi
import com.scribblex.rssi.data.local.RssiDataSource
import javax.inject.Inject

class RssiRepository @Inject constructor(
    private val rssiDataSource: RssiDataSource
) {
    val wirelessScanResults = MutableLiveData<List<Rssi>>()

    private val observer = Observer<List<Rssi>> {
        wirelessScanResults.value = it
    }

    init {
        rssiDataSource.wirelessScanResults.observeForever(observer)
    }

    fun initWirelessScan() = rssiDataSource.initWirelessScan()

    fun setWirelessScanResults(result: MutableLiveData<List<Rssi>>) {
        wirelessScanResults.value = result.value
    }

    fun unRegisterObserver() {
        rssiDataSource.wirelessScanResults.removeObserver(observer)
    }

}