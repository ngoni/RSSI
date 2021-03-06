package com.scribblex.rssi.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.scribblex.rssi.R
import com.scribblex.rssi.databinding.ActivityMainBinding
import com.scribblex.rssi.services.RssiBackgroundService
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "MainActivity"

/**
 *
 * Entry point to creating our long running service.
 * To stop the service - force close the application.
 *
 * */

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var backgroundServiceIntent: Intent

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val PERMISSION_LOCATION_WIFI_STATE = 1
    }

    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // request permissions
        // assumptions: we are only handling the success use-case for requesting permissions
        if (hasRequiredPermissions()) {
            startBackgroundService()
            observeViewModel()
        } else {
            requestRequiredPermissions()
        }
    }

    private fun observeViewModel() {
        viewModel.observeRssiRepositoryChanges()
        viewModel.getViewState().observe(this) { it ->

            val stringBuilder = StringBuilder()
            stringBuilder.append("Wifi Scan Time: ${getCurrentDate()} \n")
            stringBuilder.append("\n")

            it.forEach {
                stringBuilder.append("SSID: ${it.ssid}, Strength: ${it.strength} \n")
            }
            binding.requestPayload.text = stringBuilder.toString()
        }
    }

    private fun startBackgroundService() {
        Log.d(TAG, "Starting background service")
        backgroundServiceIntent = Intent(this, RssiBackgroundService::class.java).also {
            startService(it)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "Required permissions denied!!")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            startBackgroundService()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "Required permissions granted!!")
        startBackgroundService()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun hasRequiredPermissions() = EasyPermissions.hasPermissions(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE
    )

    private fun requestRequiredPermissions() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.permissions_rationale),
            PERMISSION_LOCATION_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE
        )
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.US)
        return sdf.format(Date())
    }

}