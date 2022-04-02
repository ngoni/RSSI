package com.scribblex.rssi.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.scribblex.rssi.data.repository.ApiRepository
import com.scribblex.rssi.data.repository.RssiRepository
import com.scribblex.rssi.workers.RssiBackgroundWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This is a background service that uses WorkManager
 * to perform periodic work
 */

private const val TAG = "RSSIBackgroundService"

@AndroidEntryPoint
class RSSIBackgroundService : LifecycleService() {

    @Inject
    lateinit var rssiRepository: RssiRepository

    @Inject
    lateinit var apiRepository: ApiRepository

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommand()")
        startBackgroundWork()
        startWifiScanResultsObserver()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun startBackgroundWork() {
        Log.d(TAG, "Starting background work")
//        setupWorkManager()
        rssiRepository.initWirelessScan()
    }

    private fun setupWorkManager() {
        Log.d(TAG, "Setup WorkManager")

        // https://developer.android.com/guide/topics/connectivity/wifi-scan
        // this value is based off recommended Google Practices for Wifi-scanning
        val repeatInterval: Long = 15
        val uniqueWorkName = "StartRssiBackgroundWork"
        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            RssiBackgroundWorker::class.java,
            repeatInterval, TimeUnit.MINUTES
        ).build()

        val workManager: WorkManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    /**
     * The below function listens for RSSI updates from the Wifi Scan operation.
     * The received data is then supposed to be sent to an Imaginary API
     *
     * @see RssiPayload - this represents the format of the payload in Json Schema.
     * The formatting is done using Gson library
     *
     * Testing of this section of code wasn't possible as I didn't have an API to use.
     * Theoretically the logic below should work.
     * **/

    private fun startWifiScanResultsObserver() {
        rssiRepository.wirelessScanResults.observe(this) {
            lifecycleScope.launch {
                apiRepository.sendRssiData(it)
            }
        }
    }

}