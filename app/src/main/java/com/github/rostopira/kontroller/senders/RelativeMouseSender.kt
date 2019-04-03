package com.github.rostopira.kontroller.senders

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import com.github.rostopira.kontroller.reports.TrackpadMouseReport
import java.util.*
import kotlin.concurrent.schedule

@Suppress("MemberVisibilityCanBePrivate")
open class RelativeMouseSender(
    val hidDevice: BluetoothHidDevice,
    val host: BluetoothDevice

) {
    val mouseReport = TrackpadMouseReport()



    protected open fun sendMouse() {
        if (!hidDevice.sendReport(host, TrackpadMouseReport.ID, mouseReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    fun sendTestMouseMove() {
        mouseReport.dxLsb = 20
        mouseReport.dyLsb = 20
        mouseReport.dxMsb = 20
        mouseReport.dyMsb = 20
        sendMouse()
    }

    fun sendTestClick() {
        mouseReport.leftButton = true
        sendMouse()
        Timer().schedule(50L) {
            mouseReport.leftButton = false
            sendMouse()
        }
    }



    companion object {
        const val TAG = "TrackPadSender"
    }

}