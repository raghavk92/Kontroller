package com.github.rostopira.kontroller.senders



import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import android.view.KeyEvent
import com.github.rostopira.kontroller.reports.KeyboardReport
import com.github.rostopira.kontroller.reports.ScrollableTrackpadMouseReport
import java.util.*
import kotlin.concurrent.schedule

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

    fun sendKeyboard(keyCode : Int, event : KeyEvent, modifier_checked_state :Int): Boolean {

        return when (event.keyCode) {
            KeyEvent.KEYCODE_A -> {
                if(event.isShiftPressed) keyboardReport.leftShift=true
                //else keyboardReport.leftShift = false
                if(event.isAltPressed) keyboardReport.leftAlt=true
                //else keyboardReport.leftAlt=false
                if(event.isCtrlPressed) keyboardReport.leftControl=true
                //else keyboardReport.leftControl=false
                if(event.isMetaPressed) keyboardReport.leftGui=true
                //else keyboardReport.leftGui=false

                keyboardReport.key1=KeyboardReport.KEYCODE_A.toByte()
                customSender(modifier_checked_state)

                true
            }

            KeyEvent.KEYCODE_B -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_B.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_C -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_C.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_D -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_D.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_E -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_E.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_G -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_G.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_H -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_H.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_I -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_I.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_J -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_J.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_K -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_K.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_L -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_L.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_M -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_M.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_N -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_N.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_O -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_O.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_P -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_P.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_Q -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_Q.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_R -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_R.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_S -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_S.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_T -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_T.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_U -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_U.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_V -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_V.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_W -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_W.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_X -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_X.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_Y -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_Y.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_Z -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_Z.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_TAB -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_TAB.toByte()
                sendKeys()
                if(modifier_checked_state==0) sendNullKeys()
                else {

                    keyboardReport.key1=0.toByte()
                    sendKeys()
                }

                true
            }
            KeyEvent.KEYCODE_0 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_0.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_1 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_1.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_2 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_2.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_3 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_3.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_4 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_4.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_5 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_5.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_6 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_6.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_7 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_7.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_8 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_8.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_9 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_9.toByte()
                customSender(modifier_checked_state)

                true
            }

            KeyEvent.KEYCODE_GRAVE -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_GRAVE_ACCENT.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_GRAVE -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_GRAVE_ACCENT.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_AT -> {
                setModifiers(event)
                keyboardReport.leftShift=true
                keyboardReport.key1=KeyboardReport.KEYCODE_SHIFT_AT.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_POUND -> {
                setModifiers(event)
                keyboardReport.leftShift=true
                keyboardReport.key1=KeyboardReport.KEYCODE_SHIFT_POUND.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_STAR -> {
                setModifiers(event)
                keyboardReport.leftShift=true
                keyboardReport.key1=KeyboardReport.KEYCODE_SHIFT_ASTERIX.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_MINUS -> {
                setModifiers(event)
                keyboardReport.leftShift=true
                keyboardReport.key1=KeyboardReport.KEYCODE_DASH.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_MINUS -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_DASH.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_EQUALS -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_EQUALS.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_DEL -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_BACKSPACE.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_ENTER -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_ENTER.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_ESCAPE -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_ESCAPE.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_SPACE -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_SPACEBAR.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_LEFT_BRACKET -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_OPEN_BRACKET.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_RIGHT_BRACKET -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_CLOSED_BRACKET.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_BACKSLASH -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_BACKSLASH.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_SEMICOLON -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_SEMICOLON.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_APOSTROPHE -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_APOSTROPHE.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_COMMA -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_COMMA.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_PERIOD -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_PERIOD.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_SLASH -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_FORWARD_SLASH.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_LEFT_ARROW.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_RIGHT_ARROW.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_UP_ARROW.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_DOWN_ARROW.toByte()
                customSender(modifier_checked_state)

                true
            }

            KeyEvent.KEYCODE_F1 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F1.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F2 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F2.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F3 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F3.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F4 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F4.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F5 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F5.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F6 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F6.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F7 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F7.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F8 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F8.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F9 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F9.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F10 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F10.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F11 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F11.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_F12 -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_F12.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_SCROLL_LOCK -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_SCROLL_LOCK.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_NUM_LOCK -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_NUM_LOCK.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_PAGE_UP -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_PAGE_UP.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_PAGE_DOWN -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_PAGE_DOWN.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_MOVE_END -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_END.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_MOVE_HOME -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_HOME.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_INSERT -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_INSERT.toByte()
                customSender(modifier_checked_state)

                true
            }
            KeyEvent.KEYCODE_FORWARD_DEL -> {
                setModifiers(event)

                keyboardReport.key1=KeyboardReport.KEYCODE_FORWARD_DELETE.toByte()
                customSender(modifier_checked_state)

                true
            }






//            KeyEvent.KEYCODE_F -> {
//                if(event.isShiftPressed)
//                {
//
//                    keyboardReport.leftShift=true
//                }
//                if(event.isAltPressed)
//                {
//                    keyboardReport.leftAlt=true
//                }
//
//                if(event.isCtrlPressed)
//                {
//                    keyboardReport.leftControl
//
//                }
//                if(event.isMetaPressed)
//                {
//                    keyboardReport.leftGui=true
//                }
//
//                keyboardReport.key1=KeyboardReport.KEYCODE_F.toByte()
//                hidDevice.sendReport(host,KeyboardReport.ID,keyboardReport.bytes)
//                sendNullKeys()
//                Log.d("keyboardyahanhai", "this is D")
//
//                Log.d("keyboardyahanhai", "this is F")
//                true
//            }


            else -> false
        }

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