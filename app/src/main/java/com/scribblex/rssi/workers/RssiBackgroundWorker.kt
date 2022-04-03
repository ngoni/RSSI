package com.scribblex.rssi.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.scribblex.rssi.data.repository.RssiRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

private const val TAG = "RssiBackgroundWorker"

@HiltWorker
class RssiBackgroundWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {

    @Inject
    lateinit var repository: RssiRepository

    override fun doWork(): Result {
        Log.d(TAG, "WorkManger: Executing RssiBackgroundWorker")
        repository.initWirelessScan()
        return Result.success()
    }
}