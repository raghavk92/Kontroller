package com.github.rostopira.kontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.github.rostopira.kontroller.listeners.CompositeListener
import com.github.rostopira.kontroller.listeners.GestureDetectListener
import com.github.rostopira.kontroller.senders.RelativeMouseSender
import com.github.rostopira.kontroller.senders.SensorSender
import com.github.rostopira.kontroller.listeners.ViewListener
import org.jetbrains.anko.*
import com.github.rostopira.kontroller.extraLibraries.CustomGestureDetector
import com.github.rostopira.kontroller.senders.KeyboardSender
import android.widget.CheckBox

import android.content.SharedPreferences;
import android.preference.PreferenceManager;




class SelectDeviceActivity: Activity(),KeyEvent.Callback {

    private var autoPairMenuItem : MenuItem? =null

    private lateinit var linearLayout: _LinearLayout
    private var sender: SensorSender? = null
    //private var  viewTouchListener : ViewListener? = null
    private var modifier_checked_state : Int =0
    private var  rMouseSender : RelativeMouseSender? = null

    private var rKeyboardSender : KeyboardSender? = null


    @SuppressLint("ResourceType")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            verticalLayout {



                        // justify your toolbar






                linearLayout = this
                id = 0x69
                //gravity = Gravity.CENTER
//                button("TEST") {
//                    setOnClickListener {
//                        rMouseSender?.sendTestClick() ?: toast("Not connected")
//
//                    }
//                }



                view(){
                  id= R.id.mouseView
                  background=getDrawable(R.drawable.view_border)


                }.lparams(width= matchParent,height = matchParent )

            }


//        var x= -2047
//        var baos : ByteArrayOutputStream = ByteArrayOutputStream()
//        var daos : DataOutputStream = DataOutputStream(baos)
//
//        daos.writeInt(x)
//        daos.close()
//        val bytes : ByteArray = baos.toByteArray()
//        Log.i("dd",bytes[3].toString())
//
//
//        val bytes1 : ByteArray = ByteArray(4){0}
//        val buff : ByteBuffer = ByteBuffer.wrap(bytes1)
//        buff.putInt(x)
//        Log.i("dd1",bytes1.contentToString())
//        val buff1 : ByteBuffer = ByteBuffer.wrap(bytes1)
//        Log.i("dd1",buff1.getInt().toString())
//
//        val bytes2 : ByteArray = ByteArray(2){0}
//        val buff3 : ByteBuffer = ByteBuffer.wrap(bytes2)
//        buff3.putShort(-2047)
//        //bytes2[0]= bytes1[2]
//        //bytes2[1]= bytes1[3]
//        Log.i("dd2",bytes2[0].toString())
//        val buff2 : ByteBuffer = ByteBuffer.wrap(bytes2)
//
//        Log.i("dd2",buff2.getShort().toString())


    }

    fun getContext(): Context {
        return this
    }




    public override fun onStart() {
        super.onStart()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        BluetoothController.autoPairFlag= sharedPref.getBoolean(getString(R.string.auto_pair_flag),false)
        if(autoPairMenuItem!=null)
        {
            autoPairMenuItem?.isChecked= sharedPref.getBoolean(getString(R.string.auto_pair_flag),false)
        }



        val trackPadView = find<View>(R.id.mouseView)





        BluetoothController.init(this)

        BluetoothController.getSender { hidd, device ->
            Log.wtf("weiufhas", "Callback called")
            val mainHandler = Handler(getContext().mainLooper)

            mainHandler.post(object : Runnable{
                override fun run() {

                    ////to be removed
                    rKeyboardSender= KeyboardSender(hidd,device)


                    ///  end removed


                    val rMouseSender = RelativeMouseSender(hidd,device)
                    Log.i("TAGdddUI", Thread.currentThread().getName());
                    val viewTouchListener = ViewListener(hidd, device, rMouseSender)
                    val mDetector = CustomGestureDetector(getContext(), GestureDetectListener(rMouseSender))

                    val gTouchListener = object : View.OnTouchListener {

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    return mDetector.onTouchEvent(event)

                }

            }




                    val composite : CompositeListener = CompositeListener()

                    composite.registerListener(gTouchListener)
                    composite.registerListener(viewTouchListener)
            trackPadView.setOnTouchListener(composite)











                    //------------trackPadView.setOnTouchListener(viewTouchListener)
                }

            })



            //========val rMouseSender = RelativeMouseSender(hidd,device)
            //-------this.rMouseSender=rMouseSender
           //val mDetector = GestureDetector(this, GestureDetectListener(rMouseSender))

            Log.i("TAGddd", Thread.currentThread().getName());
            //--------------val viewTouchListener = ViewListener(hidd, device, rMouseSender)//=
//            val gTouchListener = object : View.OnTouchListener {
//
//                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//
//                    return mDetector.onTouchEvent(event)
//
//                }
//
//            }


//
            ////////-----trackPadView.setOnTouchListener(viewTouchListener)
//            myView.setOnClickListener {object : View.OnClickListener {
//                override fun onClick(v: View?) {
//                    rMouseSender.sendTestClick()
//                }
//
//
//            }}

//            val composite : CompositeListener = CompositeListener()
//            composite.registerListener(viewTouchListener)
//            composite.registerListener(gTouchListener)
//            myView.setOnTouchListener(composite)




         //   sender = SensorSender(hidd, device)
         //   initSensor()
        }

    }

    private fun initSensor() {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        sensorManager.registerListener(sender, sensor, SensorManager.SENSOR_DELAY_GAME)
    }



    public override fun onPause() {
        super.onPause()

    }

    public override fun onStop() {
        super.onStop()
        BluetoothController.btHid?.unregisterApp()

        BluetoothController.hostDevice=null
        BluetoothController.btHid=null
    }


    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {

       // val trackPadView = find<View>(R.id.mouseView)

        menuInflater.inflate(R.menu.select_device_activity_menu, menu)
        autoPairMenuItem= menu?.findItem(R.id.action_autopair)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        Log.i("crown","jewel")
        autoPairMenuItem?.isChecked= sharedPref.getBoolean(getString(R.string.auto_pair_flag),false)


//        val checkBox = menu?.findItem(R.id.check_modifier_state)?.actionView as CheckBox
//        checkBox.text = "Modifier Released"

        //getMenuInflater().inflate(R.menu.select_device_activity_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

//    companion object {
//        fun callAlert(){
//            val builder = AlertDialog.Builder(SelectDeviceActivity.this)
//            builder.setTitle("Make your selection")
//            builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
//                // Do something with the selection
//                mDoneButton.setText(items[item])
//            })
//            val alert = builder.create()
//            alert.show()
//        }
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        Log.d("keyeventdown_tag","desc is - $event")


        if(rKeyboardSender !=null && event !=null) {
            var rvalue: Boolean? = false
            //rvalue = rKeyboardSender?.sendKeyboard(keyCode, event,modifier_checked_state)

            if (rvalue == true) return true


            else return super.onKeyDown(keyCode, event)

        }
        else return super.onKeyDown(keyCode, event)


    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {


        Log.d("keyeventup_tag","desc is - $event")

        if(rKeyboardSender !=null && event !=null) {
            var rvalue: Boolean? = false
            rvalue = rKeyboardSender?.sendKeyboard(keyCode, event,modifier_checked_state)

            if (rvalue == true) return true


            else return super.onKeyDown(keyCode, event)

        }
        else return super.onKeyUp(keyCode, event)


    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.action_keyboard -> {




                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)




            true
        }

        R.id.check_modifier_state -> {

//            item.isChecked = !item.isChecked
//            Log.i("bbbb","${item.isChecked}")
//            if(item.isChecked)
//                modifier_checked_state=1
//            else modifier_checked_state=0
            if(modifier_checked_state==1)
            {
                modifier_checked_state=0
                item.title="MODIFIER RELEASED"
                rKeyboardSender?.sendNullKeys()

            }

            else
            {
                modifier_checked_state=1
                item.title="MODIFIER PRESSED"

            }



            true
        }
        R.id.action_disconnect -> {

            BluetoothController.btHid?.disconnect(BluetoothController.hostDevice)
            true
        }

        R.id.action_autopair -> {
            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
            if(item.isChecked) {
                item.isChecked = false
                BluetoothController.autoPairFlag=false

                with(sharedPref.edit())
                {
                    putBoolean(getString(R.string.auto_pair_flag), BluetoothController.autoPairFlag)
                    commit()
                }

            }
            else
            {
                item.isChecked=true
                BluetoothController.autoPairFlag=true
                if(BluetoothController.btHid?.getConnectionState(BluetoothController.mpluggedDevice)==0 && BluetoothController.mpluggedDevice!= null && BluetoothController.autoPairFlag ==true)
                {
                    BluetoothController.btHid?.connect(BluetoothController.mpluggedDevice)
                    //hostDevice.toString()
                }
                with(sharedPref.edit())
                {
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