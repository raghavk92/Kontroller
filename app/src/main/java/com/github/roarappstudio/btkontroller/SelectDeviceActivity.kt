package com.github.roarappstudio.btkontroller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.roarappstudio.btkontroller.extraLibraries.CustomGestureDetector
import com.github.roarappstudio.btkontroller.listeners.CompositeListener
import com.github.roarappstudio.btkontroller.listeners.GestureDetectListener
import com.github.roarappstudio.btkontroller.senders.KeyboardSender
import com.github.roarappstudio.btkontroller.senders.RelativeMouseSender
import com.github.roarappstudio.btkontroller.senders.SensorSender
import kotlinx.android.synthetic.main.kontroller_main_activity.*

import org.jetbrains.anko.*
import java.util.*
import kotlin.concurrent.timerTask


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


    /**
     * If true Gyroscope control is disabled
     */
    private var trackpad_mode_enabled: Boolean = false

    /**
     * If true Keyboard should be displayed
     */
    private var display_keyboard: Boolean = true

    private var sensor : Sensor? = null


    @SuppressLint("ResourceType")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kontroller_main_activity)
    }
    fun getContext(): Context {
        return this
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
        var trackpadModeEnabledMenuItem = menu?.findItem(R.id.change_trackpad_mode)
        if(trackpad_mode_enabled==true){
            trackpadModeEnabledMenuItem?.title = "(T)"
            middle_button.visibility=View.GONE
        }

        screenOnMenuItem = menu?.findItem(R.id.action_screen_on)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        screenOnMenuItem?.isChecked =
            sharedPref.getBoolean(getString(R.string.screen_on_flag), false);
        autoPairMenuItem?.isChecked =
            sharedPref.getBoolean(getString(R.string.auto_pair_flag), false)

        var timer = Timer()

        timer.schedule(timerTask { //TODO I've no clue where to put this without a timer, if the keyboard should be enabled on startup.
            if(display_keyboard==true) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
            }
        },150)
        return super.onCreateOptionsMenu(menu)
    }


    public override fun onStart() {

        super.onStart()


        bluetoothStatus?.icon = getDrawable(R.drawable.ic_action_app_not_connected)
        bluetoothStatus?.tooltipText = "App not connected via bluetooth"

        val sharedPref =  getPreferences(MODE_PRIVATE)
        trackpad_mode_enabled = sharedPref.getBoolean(getString(R.string.track_pad_mode_flag),true)
        display_keyboard = sharedPref.getBoolean(getString(R.string.keyboard_enabled),true)

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
                    var rMouseSenderLocal = RelativeMouseSender(hidd, device)
                    rMouseSender=rMouseSenderLocal

                    Log.i("TAGdddUI", Thread.currentThread().getName());
                    val mDetector =
                        CustomGestureDetector(getContext(), GestureDetectListener(rMouseSenderLocal))
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

                    if(sender!=null){ // Unregister listener when returning from standby
                        sensorManager.unregisterListener(sender)
                    }
                    sender = SensorSender(hidd, device, rMouseSenderLocal)

                    left_button.setOnTouchListener { view: View, motionEvent: MotionEvent ->
                        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                            rMouseSenderLocal.sendLeftClickOn();
                        } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                            rMouseSenderLocal.sendLeftClickOff()
                        }
                        true
                    }
                    right_button.setOnTouchListener {view: View?, motionEvent: MotionEvent? ->
                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                            rMouseSenderLocal.sendRightClickOn();
                        } else if (motionEvent?.action == MotionEvent.ACTION_UP) {
                            rMouseSenderLocal.sendRightClickOff()
                        }
                        true
                    }

                    middle_button.setOnTouchListener{view: View?, motionEvent: MotionEvent? ->

                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {

                            sender?.scrollModeOn()
                        } else if (motionEvent?.action == MotionEvent.ACTION_UP) {
                            sender?.scrollModeOff()
                        }
                        true
                    }

                    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                    if(trackpad_mode_enabled == false) {
                        sensorManager.registerListener(
                            sender,
                            sensor,
                            SensorManager.SENSOR_DELAY_GAME
                        )
                    }
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

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val action = event!!.action
        val keyCode = event.keyCode
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                println("action")
                if (action == KeyEvent.ACTION_DOWN) {
                    rMouseSender?.sendLeftClickOn()
                }else if(action== KeyEvent.ACTION_UP){
                    rMouseSender?.sendLeftClickOff()
                }
                true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (action == KeyEvent.ACTION_DOWN) {
                    rMouseSender?.sendRightClickOn()
                }else if(action==KeyEvent.ACTION_UP){
                    rMouseSender?.sendRightClickOff()
                }
                true
            }
            else -> super.dispatchKeyEvent(event)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_keyboard -> {
            val sharedPref =  getPreferences(MODE_PRIVATE)
            val editor = sharedPref.edit()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

            display_keyboard=!display_keyboard
            println(display_keyboard)
            editor.putBoolean(getString(R.string.keyboard_enabled), display_keyboard)
            editor.commit()
            true
        }

        R.id.change_trackpad_mode -> {
            val sharedPref =  getPreferences(MODE_PRIVATE)
            val editor = sharedPref.edit()
            if (trackpad_mode_enabled) { // Enable Gyroscope
                item.title = "(G)"
                if(sensor!=null){
                    sensorManager.registerListener(sender, sensor, SensorManager.SENSOR_DELAY_GAME)
                }
                rKeyboardSender?.sendNullKeys()
                middle_button.visibility=View.VISIBLE

            } else {
                if(sensor!=null){ // Disable Gyroscope
                    sensorManager.unregisterListener(sender)
                }
                item.title = "(T)"
                middle_button.visibility=View.GONE
            }

            trackpad_mode_enabled=!trackpad_mode_enabled
            editor.putBoolean(getString(R.string.track_pad_mode_flag), trackpad_mode_enabled)
            editor.commit()
            true
        }

        R.id.check_modifier_state -> {
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
            val sharedPref =  getPreferences(MODE_PRIVATE)
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
            val sharedPref =  getPreferences(MODE_PRIVATE)
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
            //
            super.onOptionsItemSelected(item)
        }
    }


}