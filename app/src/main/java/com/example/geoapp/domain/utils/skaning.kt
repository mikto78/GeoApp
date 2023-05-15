package com.example.geoapp.domain.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat

class WifiScanner(
    private val wifiManager: WifiManager
) {

    private val scanResults = mutableListOf<String>()
    private var isScanning = false
    private var scanStartTime: Long = 0

    private val scanRunnable = Runnable {
        startScanInternal()
    }

    companion object {
        const val REQUEST_CODE_SCAN_WIFI_PERMISSIONS = 2
        private const val SCAN_INTERVAL_MS = 5000L
    }


    fun startScanningIfHasPermissions(
        activity: Activity
    ) {
        if (!permissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, activity) ||
            !permissionGranted(Manifest.permission.ACCESS_WIFI_STATE, activity) ||
            !permissionGranted(Manifest.permission.CHANGE_WIFI_STATE, activity)
        ) {
            activity.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE),
                REQUEST_CODE_SCAN_WIFI_PERMISSIONS
            )
            return
        }
        startScanning()
    }

    fun startScanning() {
        if (!isScanning) {
            isScanning = true
            scanStartTime = System.currentTimeMillis()
            startScanInternal()
        }
    }

    private fun permissionGranted(
        permissionName: String,
        activity: Activity
    ): Boolean = ActivityCompat.checkSelfPermission(activity, permissionName) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun startScanInternal() {
        wifiManager.startScan()
        scanResults.clear()
        // nie wiemy tu
        val results = wifiManager.scanResults

        // do tego
        for (result in results) {
            Log.d("BSSID", result.BSSID)
            scanResults.add(result.BSSID)
        }
        isScanning = false
        val elapsed = System.currentTimeMillis() - scanStartTime
        if (elapsed < SCAN_INTERVAL_MS) {
            Handler(Looper.getMainLooper()).postDelayed(scanRunnable, SCAN_INTERVAL_MS - elapsed)
        } else {
            startScanInternal()
        }
    }

    fun getScanResults(): List<String> {
        return scanResults.toList()
    }

}



