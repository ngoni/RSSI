package com.scribblex.rssi.data.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.scribblex.rssi.MainApplication
import com.scribblex.rssi.data.entities.Rssi
import javax.inject.Inject


/***
 * Source: https://developer.android.com/guide/topics/connectivity/wifi-scan
 *
 * The code to obtain the RSSI value for network was obtained from the above source.
 * */

private const val TAG = "RssiDataSource"

class RssiDataSource @Inject constructor() {

    val wirelessScanResults = MutableLiveData<List<Rssi>>()

    private val wifiManager =
        MainApplication.appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "Wifi Scan Results Receiver")
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }

    fun initWirelessScan() {
        Log.d(TAG, "Start Wifi Scan")
        registerReceiver()
        val success = wifiManager.startScan()
        if (!success) {
            // scan failure handling
            scanFailure()
        }
    }

    private fun scanSuccess() {
        unRegisterReceiver()
        Log.d(TAG, "Wifi Scan Success")
        processWifiScanResults()
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        Log.d(TAG, "Wifi Scan Failure")
        // potentially use oder scan results
        processWifiScanResults()
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        MainApplication.appContext.registerReceiver(wifiScanReceiver, intentFilter)
    }

    private fun unRegisterReceiver() {
        MainApplication.appContext.unregisterReceiver(wifiScanReceiver)
    }

    private fun processWifiScanResults() {
        val result = mutableListOf<Rssi>()
        wifiManager.scanResults.forEach {
            result.add(Rssi(ssid = it.SSID, strength = it.level))
        }
        wirelessScanResults.value = result
    }
}