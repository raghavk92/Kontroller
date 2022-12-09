package com.github.roarappstudio.btkontroller.listeners

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.github.roarappstudio.btkontroller.senders.RelativeMouseSender
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.abs
import kotlin.math.roundToInt

class GestureDetectListener(val rMouseSender: RelativeMouseSender) :
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {


    var singleClickTimeout = 200L
    var doubleClickTimeout = 100L
    var rightClickTimeout = 100L

    var scrollAccuracy = 0.3f
    var mouseAccuracy = 1f

    var previousX = 0f
    var previousY = 0f

    /**
     * True if mouse has been moved. If true potential double tap won't be triggered after ACTION_UP
     */
    var moved = false

    /**
     * Time of the last left click. If a new Left ACTION_DOWN event occurs before a certain amount of time has passed, doubleclickdown is set to true
     */
    var lastLeftClick = System.currentTimeMillis()

    /**
     * Time a left click has been down. If it reaches a certain threshold a potential
     */
    var doubeLeftClickDownTime = System.currentTimeMillis()

    /**
     * If set to true a sendLeftClickOff is only triggered, after another ACTION_UP event
     */
    var doubleclickdown = false

    /**
     * If an ACTION_POINTER_UP event of to pointers is triggered before a certain threshold a right click will be sent
     */
    var potentialRightClickStartTime = System.currentTimeMillis()

    /**
     * If two pointers are being held down, scrolling is possible in scroll event
     */
    var twoPointerDown = false

    /**
     * If two pointers are being held down, every movement except scrolling is blocked
     */
    var scrollLock = false

    internal var downTimestamp = System.currentTimeMillis()
    fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            val action = ev.action and MotionEvent.ACTION_MASK
            if (ev.pointerCount == 1) {
                if (scrollLock) {
                    return false
                } else {
                    when (action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (System.currentTimeMillis() - lastLeftClick < doubleClickTimeout) {
                                doubleclickdown = true
                                doubeLeftClickDownTime = System.currentTimeMillis()
                                lastLeftClick = 0
                            }
                            previousX = ev.x;
                            previousY = ev.y;
                            downTimestamp = System.currentTimeMillis()
                            moved = false
                        }
                        MotionEvent.ACTION_MOVE -> {
                            var dx = -(previousX - ev.x)
                            var dy = -(previousY - ev.y)

                            if (System.currentTimeMillis() - downTimestamp > singleClickTimeout || (abs(dx)) > 5 || abs(dy) > 5
                            ) {
                                moved = true
                                rMouseSender.sendRelXY((dx*mouseAccuracy).roundToInt(), (dy*mouseAccuracy).roundToInt())
                                previousX = ev.x
                                previousY = ev.y
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            if (System.currentTimeMillis() - downTimestamp < singleClickTimeout && !moved && !doubleclickdown) {
                                lastLeftClick = System.currentTimeMillis()
                                rMouseSender.sendLeftClickOn()
                                val timer = Timer()
                                timer.schedule(timerTask {
                                    if (!doubleclickdown) {
                                        rMouseSender.sendLeftClickOff()
                                    }
                                }, doubleClickTimeout)
                            } else if (doubleclickdown) {
                                rMouseSender.sendLeftClickOff()
                                if (System.currentTimeMillis() - doubeLeftClickDownTime < doubleClickTimeout) {
                                    Thread.sleep(50)
                                    rMouseSender.sendLeftClickOn()
                                    Thread.sleep(50)
                                    rMouseSender.sendLeftClickOff()
                                    doubeLeftClickDownTime = 0
                                }
                                doubleclickdown = false
                            }
                        }
                    }
                }
            } else if (ev.pointerCount == 2) {
                when (action) {
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        twoPointerDown = true
                        potentialRightClickStartTime = System.currentTimeMillis()
                        rMouseSender.mouseReport.setRelXY(0, 0)
                        scrollLock = true
                    }
                    MotionEvent.ACTION_POINTER_UP -> {
                        twoPointerDown = false
                        if (System.currentTimeMillis() - potentialRightClickStartTime < rightClickTimeout) {
                            rMouseSender.sendRightClick()
                            potentialRightClickStartTime = 0
                        }
                        rMouseSender.mouseReport.setRelXY(0, 0)
                        rMouseSender.sendScroll(0, 0)
                        var timer = Timer()
                        timer.schedule(timerTask {
                            scrollLock = false
                        }, rightClickTimeout)
                    }
                }
            }
        }
        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.i("doubleddht", "this is on double tap $e")
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {

        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.i("doubleddhs", "this is on single tap confirmed $e")

        return false
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.i("doubleddhu", "this is on single tap up $e")
        //
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (twoPointerDown) {


            if (System.currentTimeMillis() - potentialRightClickStartTime > 100) {
                rMouseSender.mouseReport.setRelXY(0, 0)
                rMouseSender.sendScroll(
                    -((distanceY) * scrollAccuracy).roundToInt(),
                    ((distanceX) * scrollAccuracy).roundToInt()
                )
                rMouseSender.sendScroll(0,0)
            }
        }
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.d("ggkjh", "onDown: $e")
        return false

    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.i("this is a fling e1 ", "$e1")
        Log.i("this is a fling e2 ", "$e2")
        Log.i("this is a fling vx ", "$velocityX")
        Log.i("this is a fling vy ", "$velocityY")

        return false
    }


    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onShowPress(e: MotionEvent?) {

    }


}