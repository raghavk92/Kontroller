package com.github.roarappstudio.btkontroller.senders

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import com.github.roarappstudio.btkontroller.reports.ScrollableTrackpadMouseReport
import java.util.*
import kotlin.concurrent.schedule

@Suppress("MemberVisibilityCanBePrivate")
open class RelativeMouseSender(
    val hidDevice: BluetoothHidDevice,
    val host: BluetoothDevice

) {
    val mouseReport = ScrollableTrackpadMouseReport()
    var previousvscroll :Int=0
    var previoushscroll :Int =0


    protected open fun sendMouse() {
        if (!hidDevice.sendReport(host, ScrollableTrackpadMouseReport.ID, mouseReport.bytes)) {
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
        mouseReport.leftButton = false
        sendMouse()
    }
    fun sendDoubleTapClick() {
        mouseReport.leftButton = true
        sendMouse()
        Timer().schedule(100L) {
            mouseReport.leftButton = false
            sendMouse()
            Timer().schedule(100L) {
                mouseReport.leftButton = true
                sendMouse()
                Timer().schedule(100L) {
                    mouseReport.leftButton = false
                    sendMouse()
                }
            }
        }
    }

    fun sendLeftClickOn() {
        mouseReport.reset()
        mouseReport.leftButton = true
        sendMouse()
    }
    fun sendLeftClickOff() {
        mouseReport.reset()
        mouseReport.leftButton = false
        sendMouse()

    }
    fun sendRightClick() {
        mouseReport.reset()
        mouseReport.rightButton = true
        sendMouse()
        Thread.sleep(50)
        mouseReport.rightButton= false
        sendMouse()
    }

    fun sendRelXY(dx: Int, dy: Int){
        mouseReport.setRelXY(dx,dy)
        hidDevice.sendReport(host, 4, mouseReport.bytes)
    }

    fun sendScroll(
        vscroll:Int,hscroll:Int){

        mouseReport.reset()
        var hscrollmutable=0
        var vscrollmutable =0

        hscrollmutable=hscroll
        vscrollmutable= vscroll


        if(hscrollmutable > 127) hscrollmutable=127
        if(vscrollmutable > 127) vscrollmutable=127
        if(hscrollmutable < -127) hscrollmutable=-127
        if(vscrollmutable < -127) vscrollmutable=-127

        var vs:Int =(vscrollmutable)
        var hs:Int =(hscrollmutable)

        mouseReport.vScroll=vs.toByte()
        mouseReport.hScroll= hs.toByte()

        sendMouse()
    }

    fun sendRightClickOff() {
        mouseReport.reset()
        mouseReport.rightButton = false
        sendMouse()
    }

    fun sendRightClickOn() {
        mouseReport.reset()
        mouseReport.rightButton = true
        sendMouse()
    }

    companion object {
        const val TAG = "TrackPadSender"
    }

}