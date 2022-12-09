package com.github.roarappstudio.btkontroller.reports

import kotlin.experimental.and
import kotlin.experimental.or

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class FeatureReport (
    val bytes: ByteArray = ByteArray(1) {0}
) {


    var wheelResolutionMultiplier: Boolean
        get() = bytes[0] and 0b1 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b1
            else
                bytes[0] and 0b1110
        }

    var acPanResolutionMultiplier: Boolean
        get() = bytes[0] and 0b100 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b100
            else
                bytes[0] and 0b1011
        }


    fun reset() = bytes.fill(0)

    companion object {
        const val ID = 6.toByte()
    }
}