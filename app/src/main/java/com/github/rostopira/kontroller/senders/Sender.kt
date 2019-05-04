package com.github.rostopira.kontroller.senders

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import com.github.rostopira.kontroller.reports.MouseReport
import java.util.*
import kotlin.concurrent.schedule

@Suppress("MemberVisibilityCanBePrivate")
open class Sender(
    val hidDevice: BluetoothHidDevice,
    val host: BluetoothDevice
) {
    protected val mouseReport = MouseReport()

    protected open fun sendMouse() {
        if (!hidDevice.sendReport(host, MouseReport.ID, mouseReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    fun sendTestMouseMove() {
        mouseReport.dx = 20
        mouseReport.dy = 20
        sendMouse()
    }

    fun sendTestClick() {
        mouseReport.leftButton = true
        sendMouse()
        Timer().schedule(150L) {
            mouseReport.leftButton = false
            sendMouse()
        }
    }

    companion object {
        const val TAG = "Sender"
    }

}