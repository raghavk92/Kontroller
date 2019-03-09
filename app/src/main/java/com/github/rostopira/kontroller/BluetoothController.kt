package com.github.rostopira.kontroller

import android.bluetooth.*
import android.content.Context
import android.os.Build
import android.util.Log

@Suppress("MemberVisibilityCanBePrivate")
object BluetoothController: BluetoothHidDevice.Callback(), BluetoothProfile.ServiceListener {

    val btAdapter by lazy { BluetoothAdapter.getDefaultAdapter()!! }
    var btHid: BluetoothHidDevice? = null
    var hostDevice: BluetoothDevice? = null

    private var deviceListener: ((BluetoothHidDevice, BluetoothDevice)->Unit)? = null

    fun init(ctx: Context) {
        if (btHid != null)
            return
        btAdapter.getProfileProxy(ctx, this, BluetoothProfile.HID_DEVICE)
    }

    fun getSender(callback: (BluetoothHidDevice, BluetoothDevice)->Unit) {
        btHid?.let { hidd ->
            hostDevice?.let { host ->
                callback(hidd, host)
                return
            }
        }
        deviceListener = callback
    }

    /*****************************************************/
    /** BluetoothProfile.ServiceListener implementation **/
    /*****************************************************/

    override fun onServiceDisconnected(profile: Int) {
        Log.e(TAG, "Service disconnected!")
        if (profile == BluetoothProfile.HID_DEVICE)
            btHid = null
    }

    override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
        Log.i(TAG, "Connected to service")
        if (profile != BluetoothProfile.HID_DEVICE) {
            Log.wtf(TAG, "WTF? $profile")
            return
        }
        val btHid = proxy as? BluetoothHidDevice
        if (btHid == null) {
            Log.wtf(TAG, "WTF? Proxy received but it's not BluetoothHidDevice")
            return
        }
        this.btHid = btHid
        btHid.registerApp(sdpRecord, null, qosOut, {it.run()}, this)
        btAdapter.setScanMode(BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 300000)
    }

    /************************************************/
    /** BluetoothHidDevice.Callback implementation **/
    /************************************************/

    override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int) {
        super.onConnectionStateChanged(device, state)
        Log.i(TAG, "Connection state ${when(state) {
            BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
            BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
            BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
            BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
            else -> state.toString()
        }}")
        if (state == BluetoothProfile.STATE_CONNECTED) {
            if (device != null) {
                hostDevice = device
                deviceListener?.invoke(btHid!!, device)
                deviceListener = null
            } else {
                Log.e(TAG, "Device not connected")
            }
        } else
            hostDevice = null
    }

    /*************/
    /** Garbage **/
    /*************/

    const val TAG = "BluetoothController"

    private val sdpRecord by lazy {
        BluetoothHidDeviceAppSdpSettings(
            Build.DEVICE,
            Build.PRODUCT,
            "",
            BluetoothHidDevice.SUBCLASS1_COMBO,
            MOUSE_ABSOLUTE
        )
    }

    private val MOUSE_ABSOLUTE = byteArrayOf(
        0x05.toByte(), 0x01.toByte(), //USAGE_PAGE (Digitizers)
        0x09.toByte(), 0x02.toByte(), //USAGE (Touch Screen)
        0xa1.toByte(), 0x01.toByte(), //COLLECTION (Application)
        0x85.toByte(), 0x02.toByte(), //    REPORT_ID (MOUSE)
        0x09.toByte(), 0x01.toByte(), //    USAGE (Pointer)
        0xa1.toByte(), 0x00.toByte(), //    COLLECTION (Physical)
        0x05.toByte(), 0x01.toByte(), //        USAGE_PAGE (Generic Desk..
        0x26.toByte(), 0xff.toByte(), 0x0f, //  LOGICAL_MAXIMUM (32767)
        0x75.toByte(), 0x10.toByte(), //        REPORT_SIZE (16)
        0x95.toByte(), 0x01.toByte(), //        REPORT_COUNT (1)
        0x55.toByte(), 0x0e.toByte(), //        UNIT_EXPONENT (-2)
        0x65.toByte(), 0x13.toByte(), //        UNIT (Inch,EngLinear)
        0x09.toByte(), 0x30.toByte(), //        USAGE (X)
        0x35.toByte(), 0x00.toByte(), //        PHYSICAL_MINIMUM (0)
        0x46.toByte(), 0x70.toByte(), 0x08, //  PHYSICAL_MAXIMUM (1080)
        0x81.toByte(), 0x02.toByte(), //        INPUT (Data,Var,Abs)
        0x46.toByte(), 0x00.toByte(), 0x0f, //  PHYSICAL_MAXIMUM (1920)
        0x09.toByte(), 0x31.toByte(), //        USAGE (Y)
        0x81.toByte(), 0x02.toByte(), //        INPUT (Data,Var,Abs)
        0xc0.toByte(),                //    END_COLLECTION
        0xc0.toByte() //                END_COLLECTION
    )// End

    private val qosOut by lazy {
        BluetoothHidDeviceAppQosSettings(
            BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
            800,
            9,
            0,
            11250,
            BluetoothHidDeviceAppQosSettings.MAX
        )
    }

}