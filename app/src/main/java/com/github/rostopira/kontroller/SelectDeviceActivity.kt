package com.github.rostopira.kontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import com.github.rostopira.kontroller.senders.SensorSender
import org.jetbrains.anko.*

class SelectDeviceActivity: Activity() {

    private lateinit var linearLayout: _LinearLayout
    private var sender: SensorSender? = null

    @SuppressLint("ResourceType")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollView {
            verticalLayout {
                linearLayout = this
                id = 0x69
                gravity = Gravity.CENTER
                button("TEST") {
                    setOnClickListener {
                        sender?.sendTestMouseMove() ?: toast("Not connected")
                    }
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        BluetoothController.init(this)
        BluetoothController.getSender { hidd, device ->
            Log.wtf("weiufhas", "Callback called")
            sender = SensorSender(hidd, device)
            initSensor()
        }
    }

    private fun initSensor() {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        sensorManager.registerListener(sender, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    public override fun onPause() {
        super.onPause()
    }

}