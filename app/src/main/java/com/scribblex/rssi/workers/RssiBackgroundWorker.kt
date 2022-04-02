package com.scribblex.rssi.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.scribblex.rssi.data.repository.RssiRepository
import javax.inject.Inject

private const val TAG = "RssiBackgroundWorker"

class RssiBackgroundWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    @Inject
    lateinit var repository: RssiRepository

    override fun doWork(): Result {
        Log.d(TAG, "WorkManger: Executing RssiBackgroundWorker")
       // repository.initWirelessScan()
        return Result.success()
    }
}