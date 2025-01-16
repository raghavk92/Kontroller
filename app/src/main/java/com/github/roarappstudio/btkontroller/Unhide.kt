package com.github.roarappstudio.btkontroller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

/** Extensions which expose hidden API **/

/*
fun BluetoothAdapter.setScanMode(mode: Int, duration: Long): Boolean {

for ( i in this::class.java.methods){
if(i.name == "setScanMode"){
println(i.name)
println(i.toString())
println("Parameters")
for(j in i.parameters){
println(j.name)
println(j.type)
}
}
}
this::class.java.getMethod("setScanMode", Int::class.java, Long::class.java)
.invoke(this, mode, duration) as Boolean
return true

}
 */

fun BluetoothAdapter.setScanMode(mode: Int, duration: Long): Boolean = if (android.os.Build.VERSION.SDK_INT > 29){

    this::class.java.getMethod("setScanMode", Int::class.java, Long::class.java).invoke(this, mode, duration) as Boolean
}else{

    this::class.java.getMethod("setScanMode", Int::class.java, Int::class.java).invoke(this, mode, duration.toInt()) as Boolean
}

fun BluetoothDevice.cancelBondProcess(): Boolean =
    this::class.java.getMethod("cancelBondProcess").invoke(this) as Boolean

fun BluetoothDevice.removeBond(): Boolean =
    this::class.java.getMethod("removeBond").invoke(this) as Boolean