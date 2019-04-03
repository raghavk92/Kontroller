package com.github.rostopira.kontroller

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import org.jetbrains.anko.startActivity

class SplashScreen: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0
            )
        } else {

            startActivity<SelectDeviceActivity>()

            finish()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startActivity<SelectDeviceActivity>()
        finish()
    }

}