package com.scribblex.rssi.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "RssiBackgroundWorker"

class RssiBackgroundWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        // TODO: Poll Wifi Networks
        Log.d(TAG, "Doing some background work")
        return Result.success()
    }
}