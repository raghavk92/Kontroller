package com.github.roarappstudio.btkontroller.listeners

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.github.roarappstudio.btkontroller.senders.RelativeMouseSender
import java.nio.ByteBuffer
import kotlin.math.roundToInt

@ExperimentalUnsignedTypes
class ViewListener(val hidDevice: BluetoothHidDevice, val host: BluetoothDevice, val rMouseSender : RelativeMouseSender):  View.OnTouchListener{


    //val absMouseReport = AbsMouseReport()

    private var previousX: Float = 0f
    private var previousY: Float = 0f
    private var pointerMotionStopFlag :Int =0


    override fun onTouch(v: View, event: MotionEvent): Boolean {

        //this.gDetector.onTouchEvent(event)

            val x: Float = event.x
            val y: Float = event.y
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {


                    Log.d("pointerCount_is",event.pointerCount.toString())
                    if(event.pointerCount==1) {
                        if(pointerMotionStopFlag==1) pointerMotionStopFlag=0
                        Log.d("is this working",event.pointerCount.toString())
                    var dx: Float = x - previousX
                    var dxInt: Int = dx.roundToInt()


                    if (dxInt > 2047) dxInt = 2047

                    if (dxInt < -2047) dxInt = -2047

                    var dy: Float = y - previousY
                    var dyInt: Int = dy.roundToInt()
                    if (dyInt > 2047) dyInt = 2047


                    if (dyInt < -2047) dyInt = -2047

                    var bytesArrX = ByteArray(2) { 0 }
                    var buffX: ByteBuffer = ByteBuffer.wrap(bytesArrX)
                    buffX.putShort(dxInt.toShort())

                    var bytesArrY = ByteArray(2) { 0 }
                    var buffY: ByteBuffer = ByteBuffer.wrap(bytesArrY)
                    buffY.putShort(dyInt.toShort())

                    rMouseSender.mouseReport.dxMsb = bytesArrX[0]
                    rMouseSender.mouseReport.dxLsb = bytesArrX[1]

                    rMouseSender.mouseReport.dyMsb = bytesArrY[0]
                    rMouseSender.mouseReport.dyLsb = bytesArrY[1]
                    //bytes2[0]= bytes1[2]
                    //bytes2[1]= bytes1[3]


                    Log.d("ddfuck2", bytesArrX.contentToString())



                    hidDevice.sendReport(this.host, 4, rMouseSender.mouseReport.bytes)


                    // reverse direction of rotation above the mid-line
//                if (y > height / 2) {
//                    dx *= -1
//                }
//
//                // reverse direction of rotation to left of the mid-line
//                if (x < width / 2) {
//                    dy *= -1
//                }

//                renderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
//                requestRender()
                }
                    else {
                        if(pointerMotionStopFlag==0)
                        {
                            rMouseSender.mouseReport.dxMsb =0
                            rMouseSender.mouseReport.dxLsb = 0

                            rMouseSender.mouseReport.dyMsb = 0
                            rMouseSender.mouseReport.dyLsb = 0
                            //bytes2[0]= bytes1[2]
                            //bytes2[1]= bytes1[3]


                            Log.d("ddfuck2", rMouseSender.mouseReport.dxMsb.toString() +"," +rMouseSender.mouseReport.dxLsb.toString())



                            hidDevice.sendReport(this.host, 4, rMouseSender.mouseReport.bytes)
                        }
                       pointerMotionStopFlag=1;

                    }

                }
                MotionEvent.ACTION_UP->{
                    rMouseSender.mouseReport.dxMsb =0
                    rMouseSender.mouseReport.dxLsb = 0

                    rMouseSender.mouseReport.dyMsb = 0
                    rMouseSender.mouseReport.dyLsb = 0
                    //bytes2[0]= bytes1[2]
                    //bytes2[1]= bytes1[3]


                    Log.d("up action ddf2", rMouseSender.mouseReport.dxMsb.toString() +"," +rMouseSender.mouseReport.dxLsb.toString())



                    hidDevice.sendReport(this.host, 4, rMouseSender.mouseReport.bytes)
                }
            }


            previousX = x
            previousY = y


            return true

        }
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        Log.d(TAG, "Accuracy changed ${when(accuracy) {
//            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "LOW"
//            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "MEDIUM"
//            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "HIGH"
//            else -> accuracy.toString()
//        }}")
//    }
//
//    override fun onSensorChanged(event: SensorEvent) {
//        val angleX = event.values[2].toDouble()
//        val angleY = event.values[0].toDouble()
//        val pixelsX = (angleX * 3840 / PI).roundToInt() + 1920
//        val pixelsY = (angleY * 2160 / PI).roundToInt() + 1080
//        Log.wtf("WTF", "$pixelsX x $pixelsY")
//        if (pixelsX != absMouseReport.X || pixelsY != absMouseReport.Y) {
//            absMouseReport.X = pixelsX
//            absMouseReport.Y = pixelsY
//            hidDevice.sendReport(this.host, 2, absMouseReport.bytes)
//
//        } else {
//            Log.i("WTF", "No changes")
//        }
//    }







}