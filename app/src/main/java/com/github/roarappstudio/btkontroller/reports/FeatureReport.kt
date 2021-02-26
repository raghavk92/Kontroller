package com.github.roarappstudio.btkontroller.reports

import java.lang.IllegalArgumentException
import kotlin.experimental.and
import kotlin.experimental.or

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class FeatureReport (
    val bytes: ByteArray = ByteArray(1) {0}
) {

    /**
     * Bits 0 and 1 of Feature Report
     */
    var wheelResolutionMultiplier: Byte
        get() = bytes[0] and 0b00000011
        set(value) {
            if (value > 3)
                throw IllegalArgumentException("WheelResolutionMultiplier must be between 0 and 3.")

            // reset WheelResolutionBits to 0
            bytes[0] = bytes[0] and 0b11111100.toByte()
            // set value to WheelResolutionBits
            bytes[0] = bytes[0] or value
        }

    /**
     * Bits 2 and 3 of Featue Report
     */
    var acPanResolutionMultiplier: Byte
        get() {
            val panBits = bytes[0] and 0b00001100
            return (panBits.toInt() shr 2).toByte()
        }
        set(value) {
            if (value > 3)
                throw IllegalArgumentException("PanResolutionMultiplier must be between 0 and 3.")

            // shift value to the right bits
            val panBits = (value.toInt() shl 2).toByte()

            // reset PanResolutionBits to 0
            bytes[0] = bytes[0] and 0b11110011.toByte()
            // set value to PanResolutionBits
            bytes[0] = bytes[0] or panBits
        }




    fun reset() = bytes.fill(0)

    companion object {
        const val ID = 6.toByte()
    }
}