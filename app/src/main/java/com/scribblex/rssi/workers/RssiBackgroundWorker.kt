package com.scribblex.rssi.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.scribblex.rssi.data.repository.RssiRepository

private const val TAG = "RssiBackgroundWorker"

class RssiBackgroundWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private lateinit var repository: RssiRepository

    override fun doWork(): Result {
        Log.d(TAG, "Retrieving RSSI for each wireless network near you")
        repository.getWirelessConnectionInfo()
        return Result.success()
    }
}