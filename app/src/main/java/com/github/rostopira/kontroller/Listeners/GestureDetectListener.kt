package com.github.rostopira.kontroller.Listeners

import android.view.GestureDetector
import android.view.MotionEvent
import com.github.rostopira.kontroller.senders.RelativeMouseSender

class GestureDetectListener(val rMouseSender : RelativeMouseSender) : GestureDetector.OnGestureListener {

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        rMouseSender.sendTestClick()
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowPress(e: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}