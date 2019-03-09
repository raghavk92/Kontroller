package com.github.rostopira.kontroller.reports

import kotlin.experimental.and
import kotlin.experimental.or

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class MouseReport(
    val bytes: ByteArray = ByteArray(4) {0}
) {

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

    var dx: Byte
        get() = bytes[1]
        set(value) { bytes[1] = value }

    var dy: Byte
        get() = bytes[2]
        set(value) { bytes[2] = value }

    fun reset() = bytes.fill(0)

    companion object {
        const val ID = 2
    }

}