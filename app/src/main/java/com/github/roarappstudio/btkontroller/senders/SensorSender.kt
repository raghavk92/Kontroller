package com.github.roarappstudio.btkontroller.senders

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.github.roarappstudio.btkontroller.senders.RelativeMouseSender.Companion.TAG
import java.nio.ByteBuffer
import kotlin.math.roundToInt

@ExperimentalUnsignedTypes
class SensorSender(val hidDevice: BluetoothHidDevice, val host: BluetoothDevice,val rMouseSender: RelativeMouseSender): SensorEventListener {
    var accuracy = 30f
    var scrollAccuracy = 5f
    var scrollMode=false

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "Accuracy changed ${when(accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "LOW"
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "MEDIUM"
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "HIGH"
            else -> accuracy.toString()
        }}")
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_GYROSCOPE){
            if(!scrollMode) {
                val dx: Double = -(event.values[2].toDouble() * accuracy)
                val dy: Double = -(event.values[0].toDouble() * accuracy)
                rMouseSender.sendRelXY(dx.roundToInt(), dy.roundToInt())
            }else{
                val dx: Double = (event.values[2].toDouble() * scrollAccuracy)
                val dy: Double = -(event.values[0].toDouble() * scrollAccuracy)
                rMouseSender.sendScroll(dy.roundToInt(),dx.roundToInt())
                rMouseSender.mouseReport.reset()
            }
        }

    }

    fun scrollModeOn() {
        scrollMode=true
    }

    fun scrollModeOff() {
        scrollMode=false
    }


}