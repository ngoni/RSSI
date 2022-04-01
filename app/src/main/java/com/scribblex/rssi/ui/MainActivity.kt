package com.scribblex.rssi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.scribblex.rssi.databinding.ActivityMainBinding
import com.scribblex.rssi.services.RSSIBackgroundService
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var backgroundServiceIntent: Intent

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startBackgroundService()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelBackgroundService()
    }

    private fun startBackgroundService() {
        Log.d(TAG, "Starting background service")
        backgroundServiceIntent = Intent(this, RSSIBackgroundService::class.java).also {
            startService(it)
        }
    }

    private fun cancelBackgroundService() {
        Log.d(TAG, "Stopping background service")
        stopService(backgroundServiceIntent)
    }

}