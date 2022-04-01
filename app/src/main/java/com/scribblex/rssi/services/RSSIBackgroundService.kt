package com.scribblex.rssi.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.scribblex.rssi.workers.RssiBackgroundWorker
import java.util.concurrent.TimeUnit

/**
 * This is a background service that uses WorkManager
 * to perform periodic work
 */

class RSSIBackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startBackgroundWork()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun startBackgroundWork() {
        setupWorkManager()
    }

    private fun setupWorkManager() {
        // https://developer.android.com/guide/topics/connectivity/wifi-scan
        // this value is based off recommended Google Practices for Wifi-scanning
        val repeatInterval: Long = 30
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

}