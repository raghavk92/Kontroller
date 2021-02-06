package com.github.roarappstudio.btkontroller

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.github.roarappstudio.btkontroller.reports.FeatureReport


@Suppress("MemberVisibilityCanBePrivate")
object BluetoothController: BluetoothHidDevice.Callback(), BluetoothProfile.ServiceListener {

    val featureReport = FeatureReport()



    override fun onSetReport(device: BluetoothDevice?, type: Byte, id: Byte, data: ByteArray?) {
        Log.i("setfirst","setfirst")
        super.onSetReport(device, type, id, data)
        Log.i("setreport","this $device and $type and $id and $data")

    }


    override fun onGetReport(device: BluetoothDevice?, type: Byte, id: Byte, bufferSize: Int) {

        Log.i("getbefore", "first")
        super.onGetReport(device, type, id, bufferSize)

        Log.i("get", "second")
            if (type == BluetoothHidDevice.REPORT_TYPE_FEATURE) {
                featureReport.wheelResolutionMultiplier = true
                featureReport.acPanResolutionMultiplier = true
                Log.i("getbthid","$btHid")

                 var wasrs=btHid?.replyReport(device, type, FeatureReport.ID, featureReport.bytes)
                Log.i("replysuccess flag ",wasrs.toString())
            }


    }


    val btAdapter by lazy { BluetoothAdapter.getDefaultAdapter()!! }
    var btHid: BluetoothHidDevice? = null
    var hostDevice: BluetoothDevice? = null
    var autoPairFlag = false

    var mpluggedDevice :BluetoothDevice? = null



    private var deviceListener: ((BluetoothHidDevice, BluetoothDevice)->Unit)? = null
    private var disconnectListener: (()->Unit)? = null

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


    fun getDisconnector(callback: ()->Unit) {

        disconnectListener = callback
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

                //deviceListener = null
            } else {
                Log.e(TAG, "Device not connected")
            }
        } else {
            hostDevice = null
            if(state == BluetoothProfile.STATE_DISCONNECTED)
            {
                disconnectListener?.invoke()
            }

        }
    }

    override fun onAppStatusChanged(pluggedDevice: BluetoothDevice?, registered: Boolean) {
        super.onAppStatusChanged(pluggedDevice, registered)
        if(registered)
        {
        var pairedDevices = btHid?.getDevicesMatchingConnectionStates(intArrayOf(BluetoothProfile.STATE_CONNECTING,BluetoothProfile.STATE_CONNECTED,BluetoothProfile.STATE_DISCONNECTED,BluetoothProfile.STATE_DISCONNECTING))
        Log.d("paired d", "paired devices are : $pairedDevices")
        mpluggedDevice = pluggedDevice
            if (autoPairFlag) {
                if (pluggedDevice != null && btHid?.getConnectionState(pluggedDevice) == BluetoothProfile.STATE_DISCONNECTED) {
                    btHid?.connect(pluggedDevice)
                    //hostDevice.toString()
                } else {
                    pairedDevices?.firstOrNull()?.let {
                        val pairedDState = btHid?.getConnectionState(it)
                        Log.d("paired d", pairedDState.toString())
                        if (pairedDState == BluetoothProfile.STATE_DISCONNECTED) {
                            btHid?.connect(it)
                        }
                    }
                }
            }

//            val intent = Intent("CUSTOM_ACTION")
//            intent.putExtra("DATE", Date().toString())
//            Log.d("j", "sending broadcast")
//
//            // send local broadcast
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)





        }


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
            DescriptorCollection.MOUSE_KEYBOARD_COMBO
        )
    }



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