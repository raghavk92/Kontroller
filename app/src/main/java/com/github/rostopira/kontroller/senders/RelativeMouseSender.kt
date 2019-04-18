package com.github.rostopira.kontroller.senders

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import com.github.rostopira.kontroller.reports.TrackpadMouseReport
import com.github.rostopira.kontroller.reports.TrackpadMouseScrollReport
import java.util.*
import kotlin.concurrent.schedule

@Suppress("MemberVisibilityCanBePrivate")
open class RelativeMouseSender(
    val hidDevice: BluetoothHidDevice,
    val host: BluetoothDevice

) {
    val mouseReport = TrackpadMouseScrollReport()
    var previousvscroll :Int=0
    var previoushscroll :Int =0


    protected open fun sendMouse() {
        if (!hidDevice.sendReport(host, TrackpadMouseScrollReport.ID, mouseReport.bytes)) {
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
//        Timer().schedule(20L) {
//
//        }
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
        mouseReport.leftButton = true
        sendMouse()


    }
    fun sendLeftClickOff() {
        mouseReport.leftButton = false
        sendMouse()

    }
    fun sendRightClick() {
        mouseReport.rightButton = true
        sendMouse()
        Timer().schedule(50L) {
            mouseReport.rightButton= false
            sendMouse()
        }
    }

    fun sendScroll(vscroll:Int,hscroll:Int){

        var hscrollmutable=0
        var vscrollmutable =0

        hscrollmutable=hscroll
        vscrollmutable= vscroll

//        var dhscroll= hscrollmutable-previoushscroll
//        var dvscroll= vscrollmutable-previousvscroll
//
//        dhscroll = Math.abs(dhscroll)
//        dvscroll = Math.abs(dvscroll)
//        if(dvscroll>=dhscroll)
//        {
//            hscrollmutable=0
//
//        }
//        else
//        {
//            vscrollmutable=0
//        }
        var vs:Int =(vscrollmutable)
        var hs:Int =(hscrollmutable)
        Log.i("vscroll ",vscroll.toString())
        Log.i("vs ",vs.toString())
        Log.i("hscroll ",hscroll.toString())
        Log.i("hs ",hs.toString())


        mouseReport.vScroll=vs.toByte()
        mouseReport.hScroll= hs.toByte()

        sendMouse()

//        previousvscroll=-1*vscroll
//        previoushscroll=hscroll


    }




    companion object {
        const val TAG = "TrackPadSender"
    }

}