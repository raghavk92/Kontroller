package com.github.rostopira.kontroller

import android.bluetooth.*
import android.content.Context
import android.os.Build
import android.util.Log
import com.github.rostopira.kontroller.reports.FeatureReport
import javax.security.auth.callback.Callback


@Suppress("MemberVisibilityCanBePrivate")
object BluetoothController: BluetoothHidDevice.Callback(), BluetoothProfile.ServiceListener {

    val featureReport = FeatureReport()



    override fun onSetReport(device: BluetoothDevice?, type: Byte, id: Byte, data: ByteArray?) {
        Log.i("setfirst","setfirst")
        super.onSetReport(device, type, id, data)
        Log.i("setreport","Haha $device and $type and $id and $data")

    }
    override fun onGetReport(device: BluetoothDevice?, type: Byte, id: Byte, bufferSize: Int) {

        Log.i("getbefore", "first")
        super.onGetReport(device, type, id, bufferSize)

        Log.i("get", "second")
            if (type == BluetoothHidDevice.REPORT_TYPE_FEATURE) {
                featureReport.wheelResolutionMultiplier = false
                featureReport.acPanResolutionMultiplier = false
                Log.i("bakar","$btHid")

                 var wasrs=btHid?.replyReport(device, type, FeatureReport.ID, featureReport.bytes)
                Log.i("replysuccess flag ",wasrs.toString())
            }


    }


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
        btHid.registerApp(sdpRecord, null, qosOut, {it.run()}, this)//--
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
            "Pixel HID1",
            "Mobile BController",
            "bla",
            BluetoothHidDevice.SUBCLASS1_COMBO,
            MOUSE_RELATIVE_WITH_SCROLL
        )
    }
    //BluetoothHidDevice.SUBCLASS1_COMBO
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




    private val MOUSE_RELATIVE1 = byteArrayOf(
        //MOUSE TLC
        0x05.toByte(), 0x01.toByte(),                         // USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x02.toByte(),                         // USAGE (Mouse)
        0xa1.toByte(), 0x01.toByte(),                         // COLLECTION (Application)
        0x85.toByte(), 0x04.toByte(),               //   REPORT_ID (Mouse)
        0x09.toByte(), 0x01.toByte(),                         //   USAGE (Pointer)
        0xa1.toByte(), 0x00.toByte(),                         //   COLLECTION (Physical)
        0x05.toByte(), 0x09.toByte(),                         //     USAGE_PAGE (Button)
        0x19.toByte(), 0x01.toByte(),                         //     USAGE_MINIMUM (Button 1)
        0x29.toByte(), 0x02.toByte(),                         //     USAGE_MAXIMUM (Button 2)
        0x15.toByte(), 0x00.toByte(),                         //     LOGICAL_MINIMUM (0)
        0x25.toByte(), 0x01.toByte(),                         //     LOGICAL_MAXIMUM (1)
        0x75.toByte(), 0x01.toByte(),                         //     REPORT_SIZE (1)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x02.toByte(),                         //     INPUT (Data,Var,Abs)
        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
        0x75.toByte(), 0x06.toByte(),                         //     REPORT_SIZE (6)
        0x81.toByte(), 0x03.toByte(),                         //     INPUT (Cnst,Var,Abs)
        0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x30.toByte(),                         //     USAGE (X)
        0x09.toByte(), 0x31.toByte(),                         //     USAGE (Y)
        0x15.toByte(), 0x01.toByte(),0xf8.toByte(),                         //     LOGICAL_MINIMUM (-2047)
        0x25.toByte(), 0xff.toByte(),0x07.toByte(),                         //     LOGICAL_MAXIMUM (2047)
        0x75.toByte(), 0x0c.toByte(),                         //     REPORT_SIZE (12)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)
        0xc0.toByte(),                               //   END_COLLECTION
        0xc0.toByte()                                //END_COLLECTION
    )

    private val MOUSE_RELATIVE_WITHOUT_SCROLL = byteArrayOf(
        //MOUSE TLC
        0x05.toByte(), 0x01.toByte(),                         // USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x02.toByte(),                         // USAGE (Mouse)
        0xa1.toByte(), 0x01.toByte(),                         // COLLECTION (Application)
        0x85.toByte(), 0x04.toByte(),               //   REPORT_ID (Mouse)
        0x09.toByte(), 0x01.toByte(),                         //   USAGE (Pointer)
        0xa1.toByte(), 0x00.toByte(),                         //   COLLECTION (Physical)
        0x05.toByte(), 0x09.toByte(),                         //     USAGE_PAGE (Button)
        0x19.toByte(), 0x01.toByte(),                         //     USAGE_MINIMUM (Button 1)
        0x29.toByte(), 0x02.toByte(),                         //     USAGE_MAXIMUM (Button 2)
        0x15.toByte(), 0x00.toByte(),                         //     LOGICAL_MINIMUM (0)
        0x25.toByte(), 0x01.toByte(),                         //     LOGICAL_MAXIMUM (1)
        0x75.toByte(), 0x01.toByte(),                         //     REPORT_SIZE (1)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x02.toByte(),                         //     INPUT (Data,Var,Abs)
        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
        0x75.toByte(), 0x06.toByte(),                         //     REPORT_SIZE (6)
        0x81.toByte(), 0x03.toByte(),                         //     INPUT (Cnst,Var,Abs)
        0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x30.toByte(),                         //     USAGE (X)
        0x09.toByte(), 0x31.toByte(),                         //     USAGE (Y)
        0x16.toByte(), 0x01.toByte(),0xf8.toByte(),                         //     LOGICAL_MINIMUM (-2047)
        0x26.toByte(), 0xff.toByte(),0x07.toByte(),                         //     LOGICAL_MAXIMUM (2047)
        0x75.toByte(), 0x10.toByte(),                         //     REPORT_SIZE (16)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)
        0xc0.toByte(),                               //   END_COLLECTION
        0xc0.toByte()                                //END_COLLECTION
    )


    private val MOUSE_RELATIVE_WITH_SCROLL = byteArrayOf(
        //MOUSE TLC
        0x05.toByte(), 0x01.toByte(),                         // USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x02.toByte(),                         // USAGE (Mouse)

        0xa1.toByte(), 0x01.toByte(),                         // COLLECTION (Application)
        0x05.toByte(), 0x01.toByte(),                         // USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x02.toByte(),                         // USAGE (Mouse)
        0xa1.toByte(), 0x02.toByte(),        //       COLLECTION (Logical)

        0x85.toByte(), 0x04.toByte(),               //   REPORT_ID (Mouse)
        0x09.toByte(), 0x01.toByte(),                         //   USAGE (Pointer)
        0xa1.toByte(), 0x00.toByte(),                         //   COLLECTION (Physical)
        0x05.toByte(), 0x09.toByte(),                         //     USAGE_PAGE (Button)
        0x19.toByte(), 0x01.toByte(),                         //     USAGE_MINIMUM (Button 1)
        0x29.toByte(), 0x02.toByte(),                         //     USAGE_MAXIMUM (Button 2)
        0x15.toByte(), 0x00.toByte(),                         //     LOGICAL_MINIMUM (0)
        0x25.toByte(), 0x01.toByte(),                         //     LOGICAL_MAXIMUM (1)
        0x75.toByte(), 0x01.toByte(),                         //     REPORT_SIZE (1)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x02.toByte(),                         //     INPUT (Data,Var,Abs)
        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
        0x75.toByte(), 0x06.toByte(),                         //     REPORT_SIZE (6)
        0x81.toByte(), 0x03.toByte(),                         //     INPUT (Cnst,Var,Abs)
        0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x30.toByte(),                         //     USAGE (X)
        0x09.toByte(), 0x31.toByte(),                         //     USAGE (Y)
        0x16.toByte(), 0x01.toByte(),0xf8.toByte(),                         //     LOGICAL_MINIMUM (-2047)
        0x26.toByte(), 0xff.toByte(),0x07.toByte(),                         //     LOGICAL_MAXIMUM (2047)
        0x75.toByte(), 0x10.toByte(),                         //     REPORT_SIZE (16)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)

        0xa1.toByte(), 0x02.toByte(),        //       COLLECTION (Logical)
        0x85.toByte(), 0x06.toByte(),               //   REPORT_ID (Feature)
        0x09.toByte(), 0x48.toByte(),        //         USAGE (Resolution Multiplier)

        0x15.toByte(), 0x00.toByte(),        //         LOGICAL_MINIMUM (0)
        0x25.toByte(), 0x01.toByte(),        //         LOGICAL_MAXIMUM (1)
        0x35.toByte(), 0x01.toByte(),        //         PHYSICAL_MINIMUM (1)
        0x45.toByte(), 0x04.toByte(),        //         PHYSICAL_MAXIMUM (4)
        0x75.toByte(), 0x02.toByte(),        //         REPORT_SIZE (2)
        0x95.toByte(), 0x01.toByte(),        //         REPORT_COUNT (1)

        0xb1.toByte(), 0x02.toByte(),        //         FEATURE (Data,Var,Abs)


        0x85.toByte(), 0x04.toByte(),               //   REPORT_ID (Mouse)
        //0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x38.toByte(),        //         USAGE (Wheel)

        0x15.toByte(), 0x81.toByte(),        //         LOGICAL_MINIMUM (-127)
        0x25.toByte(), 0x7f.toByte(),        //         LOGICAL_MAXIMUM (127)
        0x35.toByte(), 0x00.toByte(),        //         PHYSICAL_MINIMUM (0)        - reset physical
        0x45.toByte(), 0x00.toByte(),        //         PHYSICAL_MAXIMUM (0)
        0x75.toByte(), 0x08.toByte(),        //         REPORT_SIZE (8)
        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)
        0xc0.toByte(),              //       END_COLLECTION

        0xa1.toByte(), 0x02.toByte(),        //       COLLECTION (Logical)
        0x85.toByte(), 0x06.toByte(),               //   REPORT_ID (Feature)
        0x09.toByte(), 0x48.toByte(),        //         USAGE (Resolution Multiplier)

        0x15.toByte(), 0x00.toByte(),        //         LOGICAL_MINIMUM (0)
        0x25.toByte(), 0x01.toByte(),        //         LOGICAL_MAXIMUM (1)
        0x35.toByte(), 0x01.toByte(),        //         PHYSICAL_MINIMUM (1)
        0x45.toByte(), 0x04.toByte(),        //         PHYSICAL_MAXIMUM (4)
        0x75.toByte(), 0x02.toByte(),        //         REPORT_SIZE (2)
        0x95.toByte(), 0x01.toByte(),        //         REPORT_COUNT (1)

        0xb1.toByte(), 0x02.toByte(),        //         FEATURE (Data,Var,Abs)

        0x35.toByte(), 0x00.toByte(),        //         PHYSICAL_MINIMUM (0)        - reset physical
        0x45.toByte(), 0x00.toByte(),        //         PHYSICAL_MAXIMUM (0)
        0x75.toByte(), 0x04.toByte(),        //         REPORT_SIZE (4)
        0xb1.toByte(), 0x03.toByte(),        //         FEATURE (Cnst,Var,Abs)



        0x85.toByte(), 0x04.toByte(),               //   REPORT_ID (Mouse)
        0x05.toByte(), 0x0c.toByte(),        //         USAGE_PAGE (Consumer Devices)
        0x0a.toByte(), 0x38.toByte(), 0x02.toByte(),  //         USAGE (AC Pan)

        0x15.toByte(), 0x81.toByte(),        //         LOGICAL_MINIMUM (-127)
        0x25.toByte(), 0x7f.toByte(),        //         LOGICAL_MAXIMUM (127)
        0x75.toByte(), 0x08.toByte(),        //         REPORT_SIZE (8)
        0x95.toByte(), 0x01.toByte(),        //         REPORT_COUNT (1)
        0x81.toByte(), 0x06.toByte(),        //         INPUT (Data,Var,Rel)
        0xc0.toByte(),              //       END_COLLECTION
        0xc0.toByte(),              //       END_COLLECTION

        0xc0.toByte(),                               //   END_COLLECTION
        0xc0.toByte()                                //END_COLLECTION

    )


    private val MOUSE_RELATIVE_WITH_SCROLL_NOTSMOOTH = byteArrayOf(
        //MOUSE TLC
        0x05.toByte(), 0x01.toByte(),                         // USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x02.toByte(),                         // USAGE (Mouse)
        0xa1.toByte(), 0x01.toByte(),                         // COLLECTION (Application)
        0x85.toByte(), 0x04.toByte(),                         //   REPORT_ID (Mouse)
        0x09.toByte(), 0x01.toByte(),                         //   USAGE (Pointer)
        0xa1.toByte(), 0x00.toByte(),                         //   COLLECTION (Physical)
        0x05.toByte(), 0x09.toByte(),                         //     USAGE_PAGE (Button)
        0x19.toByte(), 0x01.toByte(),                         //     USAGE_MINIMUM (Button 1)
        0x29.toByte(), 0x02.toByte(),                         //     USAGE_MAXIMUM (Button 2)
        0x15.toByte(), 0x00.toByte(),                         //     LOGICAL_MINIMUM (0)
        0x25.toByte(), 0x01.toByte(),                         //     LOGICAL_MAXIMUM (1)
        0x75.toByte(), 0x01.toByte(),                         //     REPORT_SIZE (1)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x02.toByte(),                         //     INPUT (Data,Var,Abs)
        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
        0x75.toByte(), 0x06.toByte(),                         //     REPORT_SIZE (6)
        0x81.toByte(), 0x03.toByte(),                         //     INPUT (Cnst,Var,Abs)
        0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x30.toByte(),                         //     USAGE (X)
        0x09.toByte(), 0x31.toByte(),                         //     USAGE (Y)
        0x16.toByte(), 0x01.toByte(),0xf8.toByte(),           //     LOGICAL_MINIMUM (-2047)
        0x26.toByte(), 0xff.toByte(),0x07.toByte(),           //     LOGICAL_MAXIMUM (2047)
        0x75.toByte(), 0x10.toByte(),                         //     REPORT_SIZE (16)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)
        0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x38.toByte(),                        //      USAGE (Wheel)
        0x15.toByte(), 0x81.toByte(),                       //       LOGICAL_MINIMUM (-127)
        0x25.toByte(), 0x7f.toByte(),                       //       LOGICAL_MAXIMUM (127)
        0x35.toByte(), 0x00.toByte(),                     //         PHYSICAL_MINIMUM (0)        - reset physical
        0x45.toByte(), 0x00.toByte(),                     //         PHYSICAL_MAXIMUM (0)
        0x75.toByte(), 0x08.toByte(),                     //         REPORT_SIZE (8)
        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)


        0x05.toByte(), 0x0c.toByte(),                     //         USAGE_PAGE (Consumer Devices)
        0x0a.toByte(), 0x38.toByte(), 0x02.toByte(),      //         USAGE (AC Pan)
        0x15.toByte(), 0x81.toByte(),                     //         LOGICAL_MINIMUM (-127)
        0x25.toByte(), 0x7f.toByte(),                     //         LOGICAL_MAXIMUM (127)
        0x75.toByte(), 0x08.toByte(),                     //         REPORT_SIZE (8)
        0x95.toByte(), 0x01.toByte(),                     //         REPORT_COUNT (1)
        0x81.toByte(), 0x06.toByte(),                     //         INPUT (Data,Var,Rel)

        0xc0.toByte(),                                        //   END_COLLECTION
        0xc0.toByte()                                          //END_COLLECTION
    )

//    private val MOUSE_RELATIVE = byteArrayOf(
//
//        0x05.toByte(), 0x01.toByte(), //USAGE_PAGE (Digitizers)
//        0x09.toByte(), 0x02.toByte(), //USAGE (Touch Screen)
//        0xa1.toByte(), 0x01.toByte(), //COLLECTION (Application)
//        0x85.toByte(), 0x04.toByte(), //    REPORT_ID (MOUSE)
//        0x09.toByte(), 0x01.toByte(), //    USAGE (Pointer)
//        0xa1.toByte(), 0x00.toByte(), //    COLLECTION (Physical)
//
//        0x05.toByte(), 0x09.toByte(),                         //     USAGE_PAGE (Button)
//        0x19.toByte(), 0x01.toByte(),                         //     USAGE_MINIMUM (Button 1)
//        0x29.toByte(), 0x02.toByte(),                         //     USAGE_MAXIMUM (Button 2)
//        0x15.toByte(), 0x00.toByte(),                         //     LOGICAL_MINIMUM (0)
//        0x25.toByte(), 0x01.toByte(),                         //     LOGICAL_MAXIMUM (1)
//        0x75.toByte(), 0x01.toByte(),                         //     REPORT_SIZE (1)
//        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
//        0x81.toByte(), 0x02.toByte(),                         //     INPUT (Data,Var,Abs)
//        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
//        0x75.toByte(), 0x06.toByte(),                         //     REPORT_SIZE (6)
//        0x81.toByte(), 0x03.toByte(),                         //     INPUT (Cnst,Var,Abs)
//        0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
//        0x09.toByte(), 0x30.toByte(),                         //     USAGE (X)
//        0x09.toByte(), 0x31.toByte(),                         //     USAGE (Y)
//        0x15.toByte(), 0x81.toByte(),                         //     LOGICAL_MINIMUM (-127)
//        0x25.toByte(), 0x7f.toByte(),                         //     LOGICAL_MAXIMUM (127)
//        0x75.toByte(), 0x08.toByte(),                         //     REPORT_SIZE (8)
//        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
//        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)
//        0xc0.toByte(),                               //   END_COLLECTION
//        0xc0.toByte()                                //END_COLLECTION
//
//
//    )

    private val MOUSE_RELATIVE = byteArrayOf(
        //MOUSE TLC
        0x05.toByte(), 0x01.toByte(),                         // USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x02.toByte(),                         // USAGE (Mouse)
        0xa1.toByte(), 0x01.toByte(),                         // COLLECTION (Application)
        0x85.toByte(), 0x04.toByte(),               //   REPORT_ID (Mouse)
        0x09.toByte(), 0x01.toByte(),                         //   USAGE (Pointer)
        0xa1.toByte(), 0x00.toByte(),                         //   COLLECTION (Physical)
        0x05.toByte(), 0x09.toByte(),                         //     USAGE_PAGE (Button)
        0x19.toByte(), 0x01.toByte(),                         //     USAGE_MINIMUM (Button 1)
        0x29.toByte(), 0x02.toByte(),                         //     USAGE_MAXIMUM (Button 2)
        0x15.toByte(), 0x00.toByte(),                         //     LOGICAL_MINIMUM (0)
        0x25.toByte(), 0x01.toByte(),                         //     LOGICAL_MAXIMUM (1)
        0x75.toByte(), 0x01.toByte(),                         //     REPORT_SIZE (1)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x02.toByte(),                         //     INPUT (Data,Var,Abs)
        0x95.toByte(), 0x01.toByte(),                         //     REPORT_COUNT (1)
        0x75.toByte(), 0x06.toByte(),                         //     REPORT_SIZE (6)
        0x81.toByte(), 0x03.toByte(),                         //     INPUT (Cnst,Var,Abs)
        0x05.toByte(), 0x01.toByte(),                         //     USAGE_PAGE (Generic Desktop)
        0x09.toByte(), 0x30.toByte(),                         //     USAGE (X)
        0x09.toByte(), 0x31.toByte(),                         //     USAGE (Y)
        0x15.toByte(), 0x81.toByte(),                         //     LOGICAL_MINIMUM (-127)
        0x25.toByte(), 0x7f.toByte(),                         //     LOGICAL_MAXIMUM (127)
        0x75.toByte(), 0x08.toByte(),                         //     REPORT_SIZE (8)
        0x95.toByte(), 0x02.toByte(),                         //     REPORT_COUNT (2)
        0x81.toByte(), 0x06.toByte(),                         //     INPUT (Data,Var,Rel)
        0xc0.toByte(),                               //   END_COLLECTION
        0xc0.toByte()                                //END_COLLECTION
    )

//    private val MOUSE_TRACKPAD = byteArrayOf(
//        //TOUCH PAD input TLC
//        0x05.toByte(), 0x0d.toByte(),                         // USAGE_PAGE (Digitizers)
//        0x09.toByte(), 0x05.toByte(),                         // USAGE (Touch Pad)
//        0xa1.toByte(), 0x01.toByte(),                         // COLLECTION (Application)
//        0x85.toByte(), REPORTID_TOUCHPAD,            //   REPORT_ID (Touch pad)
//        0x09.toByte(), 0x22.toByte(),                         //   USAGE (Finger)
//        0xa1.toByte(), 0x02.toByte(),                         //   COLLECTION (Logical)
//        0x15.toByte(), 0x00.toByte(),                         //       LOGICAL_MINIMUM (0)
//        0x25.toByte(), 0x01.toByte(),                         //       LOGICAL_MAXIMUM (1)
//        0x09.toByte(), 0x47.toByte(),                         //       USAGE (Confidence)
//        0x09.toByte(), 0x42.toByte(),                         //       USAGE (Tip switch)
//        0x95.toByte(), 0x02.toByte(),                         //       REPORT_COUNT (2)
//        0x75.toByte(), 0x01.toByte(),                         //       REPORT_SIZE (1)
//        0x81.toByte(), 0x02.toByte(),                         //       INPUT (Data,Var,Abs)
//        0x95.toByte(), 0x01.toByte(),                         //       REPORT_COUNT (1)
//        0x75.toByte(), 0x02.toByte(),                         //       REPORT_SIZE (2)
//        0x25.toByte(), 0x02.toByte(),                         //       LOGICAL_MAXIMUM (2)
//        0x09.toByte(), 0x51.toByte(),                         //       USAGE (Contact Identifier)
//        0x81.toByte(), 0x02.toByte(),                         //       INPUT (Data,Var,Abs)
//        0x75.toByte(), 0x01.toByte(),                         //       REPORT_SIZE (1)
//        0x95.toByte(), 0x04.toByte(),                         //       REPORT_COUNT (4)
//        0x81.toByte(), 0x03.toByte(),                         //       INPUT (Cnst,Var,Abs)
//        0x05.toByte(), 0x01.toByte(),                         //       USAGE_PAGE (Generic Desk..
//        0x15.toByte(), 0x00.toByte(),                         //       LOGICAL_MINIMUM (0)
//        0x26.toByte(), 0xff.toByte(), 0x0f.toByte(),          //       LOGICAL_MAXIMUM (4095)
//        0x75.toByte(), 0x10.toByte(),                         //       REPORT_SIZE (16)
//        0x55.toByte(), 0x0e.toByte(),                         //       UNIT_EXPONENT (-2)
//        0x65.toByte(), 0x13.toByte(),                         //       UNIT(Inch,EngLinear)
//        0x09.toByte(), 0x30.toByte(),                         //       USAGE (X)
//        0x35.toByte(), 0x00.toByte(),                         //       PHYSICAL_MINIMUM (0)
//        0x46.toByte(), 0x90.toByte(), 0x01.toByte(),          //       PHYSICAL_MAXIMUM (400)
//        0x95.toByte(), 0x01.toByte(),                         //       REPORT_COUNT (1)
//        0x81.toByte(), 0x02.toByte(),                         //       INPUT (Data,Var,Abs)
//        0x46.toByte(), 0x13.toByte(), 0x01.toByte(),          //       PHYSICAL_MAXIMUM (275)
//        0x09.toByte(), 0x31.toByte(),                         //       USAGE (Y)
//        0x81.toByte(), 0x02.toByte(),                         //       INPUT (Data,Var,Abs)
//        0xc0.toByte(),                               //    END_COLLECTION
//        0x55.toByte(), 0x0C.toByte(),                         //    UNIT_EXPONENT (-4)
//        0x66.toByte(), 0x01.toByte(), 0x10.toByte(),                   //    UNIT (Seconds)
//        0x47.toByte(), 0xff.toByte(), 0xff.toByte(), 0x00.toByte(), 0x00.toByte(),      //     PHYSICAL_MAXIMUM (65535)
//        0x27, 0xff, 0xff, 0x00, 0x00,         //  LOGICAL_MAXIMUM (65535)
//        0x75, 0x10,                           //  REPORT_SIZE (16)
//        0x95, 0x01,                           //  REPORT_COUNT (1)
//        0x05, 0x0d,                         //    USAGE_PAGE (Digitizers)
//        0x09, 0x56,                         //    USAGE (Scan Time)
//        0x81, 0x02,                           //  INPUT (Data,Var,Abs)
//        0x09, 0x54,                         //    USAGE (Contact count)
//        0x25, 0x7f,                           //  LOGICAL_MAXIMUM (127)
//        0x95, 0x01,                         //    REPORT_COUNT (1)
//        0x75, 0x08,                         //    REPORT_SIZE (8)
//        0x81, 0x02,                         //    INPUT (Data,Var,Abs)
//        0x05, 0x09,                         //    USAGE_PAGE (Button)
//        0x09, 0x01,                         //    USAGE_(Button 1)
//        0x25, 0x01,                         //    LOGICAL_MAXIMUM (1)
//        0x75, 0x01,                         //    REPORT_SIZE (1)
//        0x95, 0x01,                         //    REPORT_COUNT (1)
//        0x81, 0x02,                         //    INPUT (Data,Var,Abs)
//        0x95, 0x07,                          //   REPORT_COUNT (7)
//        0x81, 0x03,                         //    INPUT (Cnst,Var,Abs)
//        0x05, 0x0d,                         //    USAGE_PAGE (Digitizer)
//        0x85, REPORTID_MAX_COUNT,            //   REPORT_ID (Feature)
//        0x09, 0x55,                         //    USAGE (Contact Count Maximum)
//        0x09, 0x59,                         //    USAGE (Pad TYpe)
//        0x75, 0x04,                         //    REPORT_SIZE (4)
//        0x95, 0x02,                         //    REPORT_COUNT (2)
//        0x25, 0x0f,                         //    LOGICAL_MAXIMUM (15)
//        0xb1, 0x02,                         //    FEATURE (Data,Var,Abs)
//        0x06, 0x00, 0xff,                   //    USAGE_PAGE (Vendor Defined)
//        0x85, REPORTID_PTPHQA,               //    REPORT_ID (PTPHQA)
//        0x09, 0xC5,                         //    USAGE (Vendor Usage 0xC5)
//        0x15, 0x00,                         //    LOGICAL_MINIMUM (0)
//        0x26, 0xff, 0x00,                   //    LOGICAL_MAXIMUM (0xff)
//        0x75, 0x08,                         //    REPORT_SIZE (8)
//        0x96, 0x00, 0x01,                   //    REPORT_COUNT (0x100 (256))
//        0xb1, 0x02,                         //    FEATURE (Data,Var,Abs)
//        0xc0,                               // END_COLLECTION
//        //CONFIG TLC
//        0x05, 0x0d,                         //    USAGE_PAGE (Digitizer)
//        0x09, 0x0E,                         //    USAGE (Configuration)
//        0xa1, 0x01,                         //   COLLECTION (Application)
//        0x85, REPORTID_FEATURE,             //   REPORT_ID (Feature)
//        0x09, 0x22,                         //   USAGE (Finger)
//        0xa1, 0x02,                         //   COLLECTION (logical)
//        0x09, 0x52,                         //    USAGE (Input Mode)
//        0x15, 0x00,                         //    LOGICAL_MINIMUM (0)
//        0x25, 0x0a,                         //    LOGICAL_MAXIMUM (10)
//        0x75, 0x08,                         //    REPORT_SIZE (8)
//        0x95, 0x01,                         //    REPORT_COUNT (1)
//        0xb1, 0x02,                         //    FEATURE (Data,Var,Abs
//        0xc0,                               //   END_COLLECTION
//        0x09, 0x22,                         //   USAGE (Finger)
//        0xa1, 0x00,                         //   COLLECTION (physical)
//        0x85, REPORTID_FUNCTION_SWITCH,     //     REPORT_ID (Feature)
//        0x09, 0x57,                         //     USAGE(Surface switch)
//        0x09, 0x58,                         //     USAGE(Button switch)
//        0x75, 0x01,                         //     REPORT_SIZE (1)
//        0x95, 0x02,                         //     REPORT_COUNT (2)
//        0x25, 0x01,                         //     LOGICAL_MAXIMUM (1)
//        0xb1, 0x02,                         //     FEATURE (Data,Var,Abs)
//        0x95, 0x06,                         //     REPORT_COUNT (6)
//        0xb1, 0x03,                         //     FEATURE (Cnst,Var,Abs)
//        0xc0,                               //   END_COLLECTION
//        0xc0,                               // END_COLLECTION
//        //MOUSE TLC
//        0x05, 0x01,                         // USAGE_PAGE (Generic Desktop)
//        0x09, 0x02,                         // USAGE (Mouse)
//        0xa1, 0x01,                         // COLLECTION (Application)
//        0x85, REPORTID_MOUSE,               //   REPORT_ID (Mouse)
//        0x09, 0x01,                         //   USAGE (Pointer)
//        0xa1, 0x00,                         //   COLLECTION (Physical)
//        0x05, 0x09,                         //     USAGE_PAGE (Button)
//        0x19, 0x01,                         //     USAGE_MINIMUM (Button 1)
//        0x29, 0x02,                         //     USAGE_MAXIMUM (Button 2)
//        0x25, 0x01,                         //     LOGICAL_MAXIMUM (1)
//        0x75, 0x01,                         //     REPORT_SIZE (1)
//        0x95, 0x02,                         //     REPORT_COUNT (2)
//        0x81, 0x02,                         //     INPUT (Data,Var,Abs)
//        0x95, 0x06,                         //     REPORT_COUNT (6)
//        0x81, 0x03,                         //     INPUT (Cnst,Var,Abs)
//        0x05, 0x01,                         //     USAGE_PAGE (Generic Desktop)
//        0x09, 0x30,                         //     USAGE (X)
//        0x09, 0x31,                         //     USAGE (Y)
//        0x75, 0x10,                         //     REPORT_SIZE (16)
//        0x95, 0x02,                         //     REPORT_COUNT (2)
//        0x25, 0x0a,                          //    LOGICAL_MAXIMUM (10)
//        0x81, 0x06,                         //     INPUT (Data,Var,Rel)
//        0xc0,                               //   END_COLLECTION
//        0xc0,                                //END_COLLECTION
//
//
//
//
//
//        0x05.toByte(), 0x01.toByte(), //USAGE_PAGE (Digitizers)
//        0x09.toByte(), 0x02.toByte(), //USAGE (Touch Screen)
//        0xa1.toByte(), 0x01.toByte(), //COLLECTION (Application)
//        0x85.toByte(), 0x02.toByte(), //    REPORT_ID (MOUSE)
//        0x09.toByte(), 0x01.toByte(), //    USAGE (Pointer)
//        0xa1.toByte(), 0x00.toByte(), //    COLLECTION (Physical)
//        0x05.toByte(), 0x01.toByte(), //        USAGE_PAGE (Generic Desk..
//        0x26.toByte(), 0xff.toByte(), 0x0f, //  LOGICAL_MAXIMUM (32767)
//        0x75.toByte(), 0x10.toByte(), //        REPORT_SIZE (16)
//        0x95.toByte(), 0x01.toByte(), //        REPORT_COUNT (1)
//        0x55.toByte(), 0x0e.toByte(), //        UNIT_EXPONENT (-2)
//        0x65.toByte(), 0x13.toByte(), //        UNIT (Inch,EngLinear)
//        0x09.toByte(), 0x30.toByte(), //        USAGE (X)
//        0x35.toByte(), 0x00.toByte(), //        PHYSICAL_MINIMUM (0)
//        0x46.toByte(), 0x70.toByte(), 0x08, //  PHYSICAL_MAXIMUM (1080)
//        0x81.toByte(), 0x02.toByte(), //        INPUT (Data,Var,Abs)
//        0x46.toByte(), 0x00.toByte(), 0x0f, //  PHYSICAL_MAXIMUM (1920)
//        0x09.toByte(), 0x31.toByte(), //        USAGE (Y)
//        0x81.toByte(), 0x02.toByte(), //        INPUT (Data,Var,Abs)
//        0xc0.toByte(),                //    END_COLLECTION
//        0xc0.toByte() //                END_COLLECTION
//    )// End



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