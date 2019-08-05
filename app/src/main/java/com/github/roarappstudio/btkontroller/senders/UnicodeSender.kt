package com.github.roarappstudio.btkontroller.senders



import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import android.view.KeyEvent
import com.github.roarappstudio.btkontroller.reports.KeyboardReport

@Suppress("MemberVisibilityCanBePrivate")
open class UnicodeSender(
    val hidDevice: BluetoothHidDevice,
    val host: BluetoothDevice

) {
    val keyboardReport = KeyboardReport()

    protected open fun sendKeys() {
        if (!hidDevice.sendReport(host, KeyboardReport.ID, keyboardReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    fun sendString(str: String) {
        str.forEach {
            keyboardReport.leftAlt = true
            var code = it.toInt().toString(16)
            while(code.length<4)
                code = "0$code"
            code.forEach {
                val keyCode = when(it) {
                    '0' -> KeyEvent.KEYCODE_0
                    '1' -> KeyEvent.KEYCODE_1
                    '2' -> KeyEvent.KEYCODE_2
                    '3' -> KeyEvent.KEYCODE_3
                    '4' -> KeyEvent.KEYCODE_4
                    '5' -> KeyEvent.KEYCODE_5
                    '6' -> KeyEvent.KEYCODE_6
                    '7' -> KeyEvent.KEYCODE_7
                    '8' -> KeyEvent.KEYCODE_8
                    '9' -> KeyEvent.KEYCODE_9
                    'a', 'A' -> KeyEvent.KEYCODE_A
                    'b', 'B' -> KeyEvent.KEYCODE_B
                    'c', 'C' -> KeyEvent.KEYCODE_C
                    'd', 'D' -> KeyEvent.KEYCODE_D
                    'e', 'E' -> KeyEvent.KEYCODE_E
                    'f', 'F' -> KeyEvent.KEYCODE_F
                    else -> {
                        Log.wtf("UnicodeSender", "Unexpected char $it")
                        return@forEach
                    }
                }.toByte()
                keyboardReport.key1 = keyCode
                sendKeys()
            }
            Log.wtf("UnicodeSender", "Sent Alt+$code")
            sendNullKeys()
        }
    }

    fun sendNullKeys() {
        keyboardReport.bytes.fill(0)
        if (!hidDevice.sendReport(host, KeyboardReport.ID, keyboardReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    companion object {
        const val TAG = "UnicodeSender"
    }

}