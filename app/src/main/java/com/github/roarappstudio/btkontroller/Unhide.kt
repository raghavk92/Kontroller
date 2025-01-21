package com.github.roarappstudio.btkontroller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

/** Extensions which expose hidden API **/

fun BluetoothAdapter.setScanMode(mode: Int, duration: Int): Boolean =
    try {
        this::class.java.getMethod("setScanMode", Int::class.java, Int::class.java)
            .invoke(this, mode, duration) as Boolean
    } catch (e : NoSuchMethodException) {
        false
    }

fun BluetoothDevice.cancelBondProcess(): Boolean =
    try {
        this::class.java.getMethod("cancelBondProcess").invoke(this) as Boolean
    } catch (e : NoSuchMethodException) {
        false
    }

fun BluetoothDevice.removeBond(): Boolean =
    try {
        this::class.java.getMethod("removeBond").invoke(this) as Boolean
    } catch (e : NoSuchMethodException) {
        false
    }