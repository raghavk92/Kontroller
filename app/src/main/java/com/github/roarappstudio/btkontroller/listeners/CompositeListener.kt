package com.github.roarappstudio.btkontroller.listeners

import android.view.MotionEvent
import android.view.View

class CompositeListener : View.OnTouchListener {

    private var registeredListeners : MutableList<View.OnTouchListener> = ArrayList<View.OnTouchListener>()


    fun registerListener(listener : View.OnTouchListener): Unit{
        registeredListeners.add(listener)

    }
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        for(listener:View.OnTouchListener in registeredListeners)
        {
            listener.onTouch(v,event)
        }
        return true

    }
}