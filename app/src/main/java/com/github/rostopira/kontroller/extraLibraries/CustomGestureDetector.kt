package com.github.rostopira.kontroller.extraLibraries

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import com.github.rostopira.kontroller.listeners.GestureDetectListener

class CustomGestureDetector(context: Context, internal var mListener: GestureDetectListener) :
    GestureDetector(context, mListener) {

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val consume = if (mListener != null) mListener!!.onTouchEvent(ev) else false
        return consume || super.onTouchEvent(ev)
    }


}