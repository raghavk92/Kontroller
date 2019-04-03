package com.github.rostopira.kontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.github.rostopira.kontroller.Listeners.CompositeListener
import com.github.rostopira.kontroller.Listeners.GestureDetectListener
import com.github.rostopira.kontroller.senders.RelativeMouseSender
import com.github.rostopira.kontroller.senders.SensorSender
import com.github.rostopira.kontroller.Listeners.ViewListener
import org.jetbrains.anko.*
import java.util.*
import kotlin.concurrent.schedule

class SelectDeviceActivity: Activity() {

    private lateinit var linearLayout: _LinearLayout
    private var sender: SensorSender? = null
    private var  viewTouchListener : ViewListener? = null

    private var  rMouseSender : RelativeMouseSender? = null


    @SuppressLint("ResourceType")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            verticalLayout {
                linearLayout = this
                id = 0x69
                //gravity = Gravity.CENTER
                button("TEST") {
                    setOnClickListener {
                        rMouseSender?.sendTestClick() ?: toast("Not connected")

                    }
                }



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


    public override fun onStart() {
        super.onStart()

        val trackPadView = find<View>(R.id.mouseView)


        BluetoothController.init(this)

        BluetoothController.getSender { hidd, device ->
            Log.wtf("weiufhas", "Callback called")

            val rMouseSender = RelativeMouseSender(hidd,device)
            this.rMouseSender=rMouseSender
           //val mDetector = GestureDetector(this, GestureDetectListener(rMouseSender))


            val viewTouchListener = ViewListener(hidd, device, rMouseSender)//=
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
            trackPadView.setOnTouchListener(viewTouchListener)
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

}