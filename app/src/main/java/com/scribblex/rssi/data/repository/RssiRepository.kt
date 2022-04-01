package com.scribblex.rssi.data.repository

import com.scribblex.rssi.data.local.RssiDataSource
import javax.inject.Inject

class RssiRepository @Inject constructor(
    private val rssiDataSource: RssiDataSource
) {

    fun getWirelessConnectionInfo() {
        rssiDataSource.fetchWirelessInfo()
    }

}