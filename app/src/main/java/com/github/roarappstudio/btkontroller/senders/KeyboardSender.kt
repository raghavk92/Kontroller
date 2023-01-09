package com.github.roarappstudio.btkontroller.senders



import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import android.view.KeyEvent
import com.github.roarappstudio.btkontroller.reports.KeyboardReport

@Suppress("MemberVisibilityCanBePrivate")
open class KeyboardSender(
    val hidDevice: BluetoothHidDevice,
    val host: BluetoothDevice

) {
    val keyboardReport = KeyboardReport()
    //val keyPosition : IntArray = IntArray(6){0}


    protected open fun sendKeys() {
        if (!hidDevice.sendReport(host, KeyboardReport.ID, keyboardReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    protected open fun customSender(modifier_checked_state: Int) {
        sendKeys()
        if(modifier_checked_state==0) sendNullKeys()
        else {
            keyboardReport.key1=0.toByte()
            sendKeys()
        }
    }

    protected open fun setModifiers(event:KeyEvent) {
        if(event.isShiftPressed) keyboardReport.leftShift=true
        if(event.isAltPressed) keyboardReport.leftAlt=true
        if(event.isCtrlPressed) keyboardReport.leftControl=true
        if(event.isMetaPressed) keyboardReport.leftGui=true
    }

    fun sendNullKeys() {
        keyboardReport.bytes.fill(0)
        if (!hidDevice.sendReport(host, KeyboardReport.ID, keyboardReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    fun keyEventHandler(keyEventCode: Int, event : KeyEvent, modifier_checked_state: Int,keyCode:Int): Boolean{


        val byteKey = KeyboardReport.KeyEventMap[keyEventCode]

        if(byteKey!=null)
        {
            setModifiers(event)
            if(event.keyCode== KeyEvent.KEYCODE_AT || event.keyCode== KeyEvent.KEYCODE_POUND || event.keyCode== KeyEvent.KEYCODE_STAR)
            {
                keyboardReport.leftShift=true
            }
            keyboardReport.key1=byteKey.toByte()
            customSender(modifier_checked_state)

            return true
        }
        else
        {
            return false
        }

    }


    fun sendKeyboard(keyCode : Int, event : KeyEvent, modifier_checked_state :Int): Boolean {


        return keyEventHandler(event.keyCode,event,modifier_checked_state,keyCode)


//        return when (event.keyCode) {
//            KeyEvent.KEYCODE_A -> {
//                if(event.isShiftPressed) keyboardReport.leftShift=true
//                //else keyboardReport.leftShift = false
//                if(event.isAltPressed) keyboardReport.leftAlt=true
//                //else keyboardReport.leftAlt=false
//                if(event.isCtrlPressed) keyboardReport.leftControl=true
//                //else keyboardReport.leftControl=false
//                if(event.isMetaPressed) keyboardReport.leftGui=true
//                //else keyboardReport.leftGui=false
//
//                keyboardReport.key1=KeyboardReport.KEYCODE_A.toByte()
//                customSender(modifier_checked_state)
//
//                true
//            }
//
//            KeyEvent.KEYCODE_B -> {
//                setModifiers(event)
//
//                keyboardReport.key1=KeyboardReport.KEYCODE_B.toByte()
//                customSender(modifier_checked_state)
//
//                true
//            }
//
//            else -> false
//        }

    }






//fun sendTestMouseMove() {
//    mouseReport.dxLsb = 20
//    mouseReport.dyLsb = 20
//    mouseReport.dxMsb = 20
//    mouseReport.dyMsb = 20
//    sendMouse()
//}
//
//fun sendTestClick() {
//    mouseReport.leftButton = true
//    sendMouse()
//    mouseReport.leftButton = false
//    sendMouse()
////        Timer().schedule(20L) {
////
////        }
//}
//fun sendDoubleTapClick() {
//    mouseReport.leftButton = true
//    sendMouse()
//    Timer().schedule(100L) {
//        mouseReport.leftButton = false
//        sendMouse()
//        Timer().schedule(100L) {
//            mouseReport.leftButton = true
//            sendMouse()
//            Timer().schedule(100L) {
//                mouseReport.leftButton = false
//                sendMouse()
//            }
//
//
//
//
//        }
//    }
//}
//
//
//
//fun sendLeftClickOn() {
//    mouseReport.leftButton = true
//    sendMouse()
//
//
//}
//fun sendLeftClickOff() {
//    mouseReport.leftButton = false
//    sendMouse()
//
//}
//fun sendRightClick() {
//    mouseReport.rightButton = true
//    sendMouse()
//    Timer().schedule(50L) {
//        mouseReport.rightButton= false
//        sendMouse()
//    }
//}
//
//fun sendScroll(vscroll:Int,hscroll:Int){
//
//    var hscrollmutable=0
//    var vscrollmutable =0
//
//    hscrollmutable=hscroll
//    vscrollmutable= vscroll
//
////        var dhscroll= hscrollmutable-previoushscroll
////        var dvscroll= vscrollmutable-previousvscroll
////
////        dhscroll = Math.abs(dhscroll)
////        dvscroll = Math.abs(dvscroll)
////        if(dvscroll>=dhscroll)
////        {
////            hscrollmutable=0
////
////        }
////        else
////        {
////            vscrollmutable=0
////        }
//    var vs:Int =(vscrollmutable)
//    var hs:Int =(hscrollmutable)
//    Log.i("vscroll ",vscroll.toString())
//    Log.i("vs ",vs.toString())
//    Log.i("hscroll ",hscroll.toString())
//    Log.i("hs ",hs.toString())
//
//
//    mouseReport.vScroll=vs.toByte()
//    mouseReport.hScroll= hs.toByte()
//
//    sendMouse()
//
////        previousvscroll=-1*vscroll
////        previoushscroll=hscroll
//
//
//}


    companion object {
        const val TAG = "KeyboardSender"
    }

}