package com.scribblex.rssi.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.scribblex.rssi.R
import com.scribblex.rssi.data.repository.ApiRepository
import com.scribblex.rssi.data.repository.RssiRepository
import com.scribblex.rssi.ui.MainActivity
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

    companion object {
        private const val ONGOING_NOTIFICATION = 1
        private const val FOREGROUND_SERVICE_CHANNEL = "FOREGROUND_SERVICE_CHANNEL"
        private const val FOREGROUND_CHANNEL = "Foreground Notifications"
    }

    override fun onCreate() {
        createNotificationChannel()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommand()")
        startForeground(ONGOING_NOTIFICATION, buildNotification())
        startBackgroundWork()
        startWifiScanResultsObserver()
        return START_STICKY
    }

    override fun onDestroy() {
        stopSelf()
        super.onDestroy()
    }

    private fun startBackgroundWork() {
        Log.d(TAG, "Starting background work")
        setupWorkManager()
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

    private fun buildNotification(): Notification {
        val pendingIntent: PendingIntent =
            Intent(applicationContext, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification =
            Notification.Builder(this, FOREGROUND_SERVICE_CHANNEL).apply {
                setContentTitle(getString(R.string.foreground_service_title))
                setContentIntent(pendingIntent)
                setSmallIcon(R.drawable.ic_launcher_foreground)
            }.build()
        return notification
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            FOREGROUND_SERVICE_CHANNEL,
            FOREGROUND_CHANNEL,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            lightColor = Color.BLUE
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

}