package com.github.roarappstudio.btkontroller

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import org.jetbrains.anko.startActivity

class SplashScreen: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if((
                    checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN)!= PackageManager.PERMISSION_GRANTED
                    )
            &&
            android.os.Build.VERSION.SDK_INT > 30
        )
            requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN

                ),
                0
            )
        else{
            startActivity<SelectDeviceActivity>()
            finish()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty()){
            for (res in grantResults){
                if (res!=PackageManager.PERMISSION_GRANTED){
                    return
                }
            }
            startActivity<SelectDeviceActivity>()
            finish()
        }
    }
}