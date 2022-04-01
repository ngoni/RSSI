package com.scribblex.rssi.data.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import com.scribblex.rssi.MainApplication


/***
 * Source: https://developer.android.com/guide/topics/connectivity/wifi-scan
 *
 * The code to obtain the RSSI value for network was obtained from the above source.
 * */

private const val TAG = "RssiDataSource"

object RssiDataSource {

    private val wifiManager =
        MainApplication.appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }

    fun fetchWirelessInfo() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        MainApplication.appContext.registerReceiver(wifiScanReceiver, intentFilter)

        val success = wifiManager.startScan()
        if (!success) {
            // scan failure handling
            scanFailure()
        }
    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults
        Log.d(TAG, "Scan Success: Results : $results")
        // use new scan results
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults
        Log.d(TAG, "Scan Failure: Results : $results")
        // potentially use oder scan results
    }

}