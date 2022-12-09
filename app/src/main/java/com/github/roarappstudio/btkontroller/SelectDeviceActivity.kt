package com.github.roarappstudio.btkontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.github.roarappstudio.btkontroller.listeners.CompositeListener
import com.github.roarappstudio.btkontroller.listeners.GestureDetectListener
import com.github.roarappstudio.btkontroller.senders.RelativeMouseSender
import com.github.roarappstudio.btkontroller.senders.SensorSender
import org.jetbrains.anko.*
import com.github.roarappstudio.btkontroller.extraLibraries.CustomGestureDetector
import com.github.roarappstudio.btkontroller.senders.KeyboardSender


class SelectDeviceActivity : Activity(), KeyEvent.Callback {

    private var autoPairMenuItem: MenuItem? = null
    private var screenOnMenuItem: MenuItem? = null

    private var bluetoothStatus: MenuItem? = null

    private lateinit var linearLayout: _LinearLayout
    private var sender: SensorSender? = null

    //private var  viewTouchListener : ViewListener? = null
    private var modifier_checked_state: Int = 0
    private var rMouseSender: RelativeMouseSender? = null
    private var rKeyboardSender: KeyboardSender? = null

    private var trackpad_mode_enabled: Boolean = true


    @SuppressLint("ResourceType")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            // justify your toolbar

            linearLayout = this
            id = 0x69

            textView() {
                id = R.id.mouseView
                background = getDrawable(R.drawable.view_border)
                text = "Trackpad"
                gravity = Gravity.CENTER
            }.lparams(width = matchParent, height = matchParent)

        }
    }
    fun getContext(): Context {
        return this
    }


    public override fun onStart() {
        super.onStart()
        bluetoothStatus?.icon = getDrawable(R.drawable.ic_action_app_not_connected)
        bluetoothStatus?.tooltipText = "App not connected via bluetooth"
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        BluetoothController.autoPairFlag =
            sharedPref.getBoolean(getString(R.string.auto_pair_flag), true)
        autoPairMenuItem?.isChecked =
            sharedPref.getBoolean(getString(R.string.auto_pair_flag), false)

        screenOnMenuItem?.isChecked =
            sharedPref.getBoolean(getString(R.string.screen_on_flag), false)

        if (sharedPref.getBoolean(getString(R.string.screen_on_flag), false)) window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        else getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        val trackPadView = find<View>(R.id.mouseView)

        BluetoothController.init(this)

        BluetoothController.getSender { hidd, device ->
            Log.wtf("weiufhas", "Callback called")
            val mainHandler = Handler(getContext().mainLooper)

            mainHandler.post(object : Runnable {
                override fun run() {
                    rKeyboardSender = KeyboardSender(hidd, device)
                    val rMouseSender = RelativeMouseSender(hidd, device)
                    Log.i("TAGdddUI", Thread.currentThread().getName());
                    val mDetector =
                        CustomGestureDetector(getContext(), GestureDetectListener(rMouseSender))
                    val gTouchListener = object : View.OnTouchListener {

                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            return mDetector.onTouchEvent(event)
                        }
                    }
                    val composite = CompositeListener()

                    composite.registerListener(gTouchListener)

                    trackPadView.setOnTouchListener(composite)

                    bluetoothStatus?.icon = getDrawable(R.drawable.ic_action_app_connected)
                    bluetoothStatus?.tooltipText = "App Connected via bluetooth"

                    sender = SensorSender(hidd, device, rMouseSender)

                    val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                    sensorManager.registerListener(sender, sensor, SensorManager.SENSOR_DELAY_GAME)
                }

            })
            Log.i("TAGddd", Thread.currentThread().getName());
        }

        BluetoothController.getDisconnector {
            val mainHandler = Handler(getContext().mainLooper)

            mainHandler.post(object : Runnable {
                override fun run() {
                    bluetoothStatus?.icon = getDrawable(R.drawable.ic_action_app_not_connected)
                    bluetoothStatus?.tooltipText = "App not connected via bluetooth"
                }
            })
        }


    }



    public override fun onPause() {
        super.onPause()

    }

    public override fun onStop() {
        super.onStop()
        BluetoothController.btHid?.unregisterApp()

        BluetoothController.hostDevice = null
        BluetoothController.btHid = null
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.select_device_activity_menu, menu)

        bluetoothStatus = menu?.findItem(R.id.ble_app_connection_status)
        autoPairMenuItem = menu?.findItem(R.id.action_autopair)

        screenOnMenuItem = menu?.findItem(R.id.action_screen_on)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        screenOnMenuItem?.isChecked =
            sharedPref.getBoolean(getString(R.string.screen_on_flag), false);

        autoPairMenuItem?.isChecked =
            sharedPref.getBoolean(getString(R.string.auto_pair_flag), false)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        Log.d("keyeventdown_tag", "desc is - $event")


        if (rKeyboardSender != null && event != null) {
            var rvalue: Boolean? = false

            if (rvalue == true) return true
            else return super.onKeyDown(keyCode, event)

        } else return super.onKeyDown(keyCode, event)


    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {


        Log.d("keyeventup_tag", "desc is - $event")

        if (rKeyboardSender != null && event != null) {
            var rvalue: Boolean? = false
            rvalue = rKeyboardSender?.sendKeyboard(keyCode, event, modifier_checked_state)

            if (rvalue == true) return true
            else return super.onKeyDown(keyCode, event)

        } else return super.onKeyUp(keyCode, event)


    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_keyboard -> {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            true
        }

        R.id.change_trackpad_mode -> {
            trackpad_mode_enabled = !trackpad_mode_enabled
            true
        }

        R.id.check_modifier_state -> {

//            item.isChecked = !item.isChecked
//            Log.i("bbbb","${item.isChecked}")
//            if(item.isChecked)
//                modifier_checked_state=1
//            else modifier_checked_state=0
            if (modifier_checked_state == 1) {
                modifier_checked_state = 0
                item.title = "(N)"
                rKeyboardSender?.sendNullKeys()

            } else {
                modifier_checked_state = 1
                item.title = "(P)"

            }
            true
        }
        R.id.action_disconnect -> {
            BluetoothController.btHid?.disconnect(BluetoothController.hostDevice)
            bluetoothStatus?.icon = getDrawable(R.drawable.ic_action_app_not_connected)
            bluetoothStatus?.tooltipText = "App not connected via bluetooth"
            true
        }

        R.id.action_screen_on -> {
            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
            if (item.isChecked) {
                item.isChecked = false
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.screen_on_flag), false)
                    commit()
                }
            } else {
                item.isChecked = true
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.screen_on_flag), true)
                    commit()
                }
            }
            true
        }

        R.id.action_autopair -> {
            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
            if (item.isChecked) {
                item.isChecked = false
                BluetoothController.autoPairFlag = false

                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.auto_pair_flag), BluetoothController.autoPairFlag)
                    commit()
                }

            } else {
                item.isChecked = true
                BluetoothController.autoPairFlag = true
                if (BluetoothController.btHid?.getConnectionState(BluetoothController.mpluggedDevice) == 0 && BluetoothController.mpluggedDevice != null && BluetoothController.autoPairFlag == true) {
                    BluetoothController.btHid?.connect(BluetoothController.mpluggedDevice)
                    //hostDevice.toString()
                }
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.auto_pair_flag), BluetoothController.autoPairFlag)
                    commit()
                }
            }
            true
        }


        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


}