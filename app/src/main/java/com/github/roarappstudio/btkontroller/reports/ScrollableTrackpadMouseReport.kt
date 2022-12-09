package com.github.roarappstudio.btkontroller.reports

import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.roundToInt

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class ScrollableTrackpadMouseReport (val bytes: ByteArray = ByteArray(7) {0}) {
        var leftButton: Boolean
        get() = bytes[0] and 0b1 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b1
            else
                bytes[0] and 0b110
        }

        var rightButton: Boolean
        get() = bytes[0] and 0b10 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b10
            else
                bytes[0] and 0b101
        }

        var dxLsb: Byte
        get() = bytes[1]
        set(value) { bytes[1] = value }

        var dxMsb: Byte
        get() = bytes[2]
        set(value) { bytes[2] = value }


        var dyLsb: Byte
        get() = bytes[3]
        set(value) { bytes[3] = value }

        var dyMsb: Byte
        get() = bytes[4]
        set(value) { bytes[4] = value }

        var vScroll : Byte
        get() = bytes[5]
        set(value) {
            bytes[5]=value
        }

        var hScroll : Byte
        get() = bytes[6]
        set(value) {
            bytes[6]=value
        }

    fun setRelXY (dx: Int, dy: Int){
        setRelX(dx)
        setRelY(dy)
    }


    fun setRelX( dx: Int){
        var dxInt: Int = dx
        if (dxInt > 2047) dxInt = 2047
        if (dxInt < -2047) dxInt = -2047
        var bytesArrX = ByteArray(2) { 0 }
        var buffX: ByteBuffer = ByteBuffer.wrap(bytesArrX)
        buffX.putShort(dxInt.toShort())
        dxMsb = bytesArrX[0]
        dxLsb = bytesArrX[1]
    }
    fun setRelY( dx: Int){
        var dyInt: Int = dx
        if (dyInt > 2047) dyInt = 2047
        if (dyInt < -2047) dyInt = -2047
        var bytesArrY = ByteArray(2) { 0 }
        var buffY: ByteBuffer = ByteBuffer.wrap(bytesArrY)
        buffY.putShort(dyInt.toShort())
        dyMsb = bytesArrY[0]
        dyLsb = bytesArrY[1]
    }
        fun reset() = bytes.fill(0)

        companion object {
            const val ID = 4
        }
}