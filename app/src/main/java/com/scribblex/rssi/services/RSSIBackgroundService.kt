package com.scribblex.rssi.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.scribblex.rssi.data.repository.RssiRepository
import com.scribblex.rssi.workers.RssiBackgroundWorker
import dagger.hilt.android.AndroidEntryPoint
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
    lateinit var repository: RssiRepository

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startBackgroundWork()
        startWifiResultsObserver()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun startBackgroundWork() {
        Log.d(TAG, "Starting background work")
//        setupWorkManager()
        repository.initWirelessScan()
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

    private fun startWifiResultsObserver() {
        repository.wirelessScanResults.observe(this) {
            // TODO: make network call
            Log.d(TAG, "Results: ${it.size}")
        }
    }

}