package com.github.roarappstudio.btkontroller.senders

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.github.roarappstudio.btkontroller.reports.AbsMouseReport
import kotlin.math.PI
import kotlin.math.roundToInt

@ExperimentalUnsignedTypes
class SensorSender(hidDevice: BluetoothHidDevice, host: BluetoothDevice): Sender(hidDevice, host), SensorEventListener {

    val absMouseReport = AbsMouseReport()


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "Accuracy changed ${when(accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "LOW"
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "MEDIUM"
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "HIGH"
            else -> accuracy.toString()
        }}")
    }

    override fun onSensorChanged(event: SensorEvent) {
        val angleX = event.values[2].toDouble()
        val angleY = event.values[0].toDouble()
        val pixelsX = (angleX * 3840 / PI).roundToInt() + 1920
        val pixelsY = (angleY * 2160 / PI).roundToInt() + 1080
        Log.wtf("WTF", "$pixelsX x $pixelsY")
        if (pixelsX != absMouseReport.X || pixelsY != absMouseReport.Y) {
            absMouseReport.X = pixelsX
            absMouseReport.Y = pixelsY
            hidDevice.sendReport(this.host, 2, absMouseReport.bytes)

        } else {
            Log.i("WTF", "No changes")
        }
    }

    override fun sendMouse() {

    }

}