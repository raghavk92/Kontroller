package com.github.rostopira.kontroller.reports

import android.view.KeyEvent
import kotlin.experimental.and
import kotlin.experimental.or


import java.io.ByteArrayOutputStream
import kotlin.experimental.and
import kotlin.experimental.or

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class KeyboardReport (
    val bytes: ByteArray = ByteArray(3) {0}
) {


    var leftControl: Boolean
        get() = bytes[0] and 0b1 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b1
            else
                bytes[0] and 0b11111110.toByte()
        }

    var leftShift: Boolean
        get() = bytes[0] and 0b10 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b10
            else
                bytes[0] and 0b11111101.toByte()
        }

    var leftAlt: Boolean
        get() = bytes[0] and 0b10 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b100
            else
                bytes[0] and 0b11111011.toByte()
        }
    var leftGui: Boolean
        get() = bytes[0] and 0b10 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b1000
            else
                bytes[0] and 0b11110111.toByte()
        }

    var rightControl: Boolean
        get() = bytes[0] and 0b1 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b10000
            else
                bytes[0] and 0b11101111.toByte()
        }

    var rightShift: Boolean
        get() = bytes[0] and 0b10 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b100000
            else
                bytes[0] and 0b11011111.toByte()
        }

    var rightAlt: Boolean
        get() = bytes[0] and 0b10 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b1000000
            else
                bytes[0] and 0b10111111.toByte()
        }
    var rightGui: Boolean
        get() = bytes[0] and 0b10 != 0.toByte()
        set(value) {
            bytes[0] = if (value)
                bytes[0] or 0b10000000.toByte()
            else
                bytes[0] and 0b01111111
        }

    var key1: Byte
        get() = bytes[2]
        set(value) { bytes[2] = value }

//    var key2: Byte
//        get() = bytes[3]
//        set(value) { bytes[3] = value }
//
//
//    var key3: Byte
//        get() = bytes[4]
//        set(value) { bytes[4] = value }
//
//    var key4: Byte
//        get() = bytes[5]
//        set(value) { bytes[5] = value }
//
//    var key5: Byte
//        get() = bytes[6]
//        set(value) { bytes[6] = value }
//
//    var key6: Byte
//        get() = bytes[7]
//        set(value) { bytes[7] = value }

    fun reset() = bytes.fill(0)

    companion object {
        const val ID = 8
//        const val KEYCODE_A = 4
//        const val KEYCODE_B = 5
//        const val KEYCODE_C = 6
//        const val KEYCODE_D = 7
//        const val KEYCODE_E = 8
//        const val KEYCODE_F = 9
//        const val KEYCODE_G = 10
//        const val KEYCODE_H = 11
//        const val KEYCODE_I = 12
//        const val KEYCODE_J = 13
//        const val KEYCODE_K = 14
//        const val KEYCODE_L = 15
//        const val KEYCODE_M = 16
//        const val KEYCODE_N = 17
//        const val KEYCODE_O = 18
//        const val KEYCODE_P = 19
//        const val KEYCODE_Q = 20
//        const val KEYCODE_R = 21
//        const val KEYCODE_S = 22
//        const val KEYCODE_T = 23
//        const val KEYCODE_U = 24
//        const val KEYCODE_V = 25
//        const val KEYCODE_W = 26
//        const val KEYCODE_X = 27
//        const val KEYCODE_Y = 28
//        const val KEYCODE_Z = 29
//        const val KEYCODE_1 = 30
//        const val KEYCODE_2 = 31
//        const val KEYCODE_3 = 32
//        const val KEYCODE_4 = 33
//        const val KEYCODE_5 = 34
//        const val KEYCODE_6 = 35
//        const val KEYCODE_7 = 36
//        const val KEYCODE_8 = 37
//        const val KEYCODE_9 = 38
//        const val KEYCODE_0 = 39
//
//        const val KEYCODE_ENTER = 40
//        const val KEYCODE_ESCAPE = 41
//        const val KEYCODE_BACKSPACE = 42
//        const val KEYCODE_TAB = 43
//        const val KEYCODE_SPACEBAR = 44
//        const val KEYCODE_DASH = 45
//        const val KEYCODE_EQUALS = 46
//        const val KEYCODE_OPEN_BRACKET = 47
//        const val KEYCODE_CLOSED_BRACKET = 48
//        const val KEYCODE_BACKSLASH = 49
//        const val KEYCODE_SEMICOLON = 51
//        const val KEYCODE_APOSTROPHE = 52
//        const val KEYCODE_GRAVE_ACCENT = 53
//        const val KEYCODE_COMMA = 54
//        const val KEYCODE_PERIOD = 55
//        const val KEYCODE_FORWARD_SLASH = 56
//
//        const val KEYCODE_F1 = 58
//        const val KEYCODE_F2 = 59
//        const val KEYCODE_F3 = 60
//        const val KEYCODE_F4 = 61
//        const val KEYCODE_F5 = 62
//        const val KEYCODE_F6 = 63
//        const val KEYCODE_F7 = 64
//        const val KEYCODE_F8 = 65
//        const val KEYCODE_F9 = 66
//        const val KEYCODE_F10 = 67
//        const val KEYCODE_F11 = 68
//        const val KEYCODE_F12 = 69
//
//        //const val KEYCODE_PRINT_SCREEN = 70
//        const val KEYCODE_SCROLL_LOCK = 71
//        //const val KEYCODE_PAUSE = 72
//        const val KEYCODE_INSERT = 73
//        const val KEYCODE_HOME = 74
//        const val KEYCODE_PAGE_UP = 75
//        const val KEYCODE_FORWARD_DELETE = 76
//        const val KEYCODE_END = 77
//        const val KEYCODE_PAGE_DOWN = 78
//        const val KEYCODE_NUM_LOCK = 83
//
//
//
//        const val KEYCODE_RIGHT_ARROW = 79
//        const val KEYCODE_LEFT_ARROW = 80
//        const val KEYCODE_DOWN_ARROW = 81
//        const val KEYCODE_UP_ARROW = 82
//
//        const val KEYCODE_SHIFT_AT = 31
//        const val KEYCODE_SHIFT_POUND = 32
//        const val KEYCODE_SHIFT_ASTERIX = 37


        //        const val KEYCODE_SHIFT_NOT = 30

        //        const val KEYCODE_SHIFT_DOLLAR = 33
//        const val KEYCODE_SHIFT_PERCENT = 34
//        const val KEYCODE_SHIFT_CARET = 35
//        const val KEYCODE_SHIFT_AMPERSAND = 36

        //        const val KEYCODE_SHIFT_OPEN_PARENTHESIS = 38
//        const val KEYCODE_SHIFT_CLOSED_PARENTHESIS = 39
//        const val KEYCODE_SHIFT_UNDERSCORE = 45
//        const val KEYCODE_SHIFT_PLUS = 46
//        const val KEYCODE_SHIFT_OPEN_CURLY_BRACKET = 47
//        const val KEYCODE_SHIFT_CLOSED_CURLY_BRACKET = 48
//        const val KEYCODE_SHIFT_PIPE = 49
//        const val KEYCODE_SHIFT_COLON = 51
//        const val KEYCODE_SHIFT_QUOTE = 52
//        const val KEYCODE_SHIFT_TILDE = 53
//        const val KEYCODE_SHIFT_LESS_THAN = 54
//        const val KEYCODE_SHIFT_GREATER_THAN = 55
//        const val KEYCODE_SHIFT_QUESTION_MARK = 56

        val KeyEventMap = mapOf<Int,Int>(
            KeyEvent.KEYCODE_A to 4,
            KeyEvent.KEYCODE_B to 5,
            KeyEvent.KEYCODE_C to 6,
            KeyEvent.KEYCODE_D to 7,
            KeyEvent.KEYCODE_E to 8,
            KeyEvent.KEYCODE_F to 9,
            KeyEvent.KEYCODE_G to 10,
            KeyEvent.KEYCODE_H to 11,
            KeyEvent.KEYCODE_I to 12,
            KeyEvent.KEYCODE_J to 13,
            KeyEvent.KEYCODE_K to 14,
            KeyEvent.KEYCODE_L to 15,
            KeyEvent.KEYCODE_M to 16,
            KeyEvent.KEYCODE_N to 17,
            KeyEvent.KEYCODE_O to 18,
            KeyEvent.KEYCODE_P to 19,
            KeyEvent.KEYCODE_Q to 20,
            KeyEvent.KEYCODE_R to 21,
            KeyEvent.KEYCODE_S to 22,
            KeyEvent.KEYCODE_T to 23,
            KeyEvent.KEYCODE_U to 24,
            KeyEvent.KEYCODE_V to 25,
            KeyEvent.KEYCODE_W to 26,
            KeyEvent.KEYCODE_X to 27,
            KeyEvent.KEYCODE_Y to 28,
            KeyEvent.KEYCODE_Z to 29,


            KeyEvent.KEYCODE_1 to 30,
            KeyEvent.KEYCODE_2 to 31,
            KeyEvent.KEYCODE_3 to 32,
            KeyEvent.KEYCODE_4 to 33,
            KeyEvent.KEYCODE_5 to 34,
            KeyEvent.KEYCODE_6 to 35,
            KeyEvent.KEYCODE_7 to 36,
            KeyEvent.KEYCODE_8 to 37,
            KeyEvent.KEYCODE_9 to 38,
            KeyEvent.KEYCODE_0 to 39,

            KeyEvent.KEYCODE_F1 to 58,
            KeyEvent.KEYCODE_F2 to 59,
            KeyEvent.KEYCODE_F3 to 60,
            KeyEvent.KEYCODE_F4 to 61,
            KeyEvent.KEYCODE_F5 to 62,
            KeyEvent.KEYCODE_F6 to 63,
            KeyEvent.KEYCODE_F7 to 64,
            KeyEvent.KEYCODE_F8 to 65,
            KeyEvent.KEYCODE_F9 to 66,
            KeyEvent.KEYCODE_F10 to 67,
            KeyEvent.KEYCODE_F11 to 68,
            KeyEvent.KEYCODE_F12 to 69,

            KeyEvent.KEYCODE_ENTER to 40,
            KeyEvent.KEYCODE_ESCAPE to 41,
            KeyEvent.KEYCODE_DEL to 42,
            KeyEvent.KEYCODE_TAB to 43,
            KeyEvent.KEYCODE_SPACE to 44,
            KeyEvent.KEYCODE_MINUS to 45,
            KeyEvent.KEYCODE_EQUALS to 46,
            KeyEvent.KEYCODE_LEFT_BRACKET to 47,
            KeyEvent.KEYCODE_RIGHT_BRACKET to 48,
            KeyEvent.KEYCODE_BACKSLASH to 49,
            KeyEvent.KEYCODE_SEMICOLON to 51,
            KeyEvent.KEYCODE_APOSTROPHE to 52,
            KeyEvent.KEYCODE_GRAVE to 53,
            KeyEvent.KEYCODE_COMMA to 54,
            KeyEvent.KEYCODE_PERIOD to 55,
            KeyEvent.KEYCODE_SLASH to 56,

            KeyEvent.KEYCODE_SCROLL_LOCK to 71,
            KeyEvent.KEYCODE_INSERT to 73,
            KeyEvent.KEYCODE_HOME to 74,
            KeyEvent.KEYCODE_PAGE_UP to 75,
            KeyEvent.KEYCODE_FORWARD_DEL to 76,
            KeyEvent.KEYCODE_MOVE_END to 77,
            KeyEvent.KEYCODE_PAGE_DOWN to 78,
            KeyEvent.KEYCODE_NUM_LOCK to 83,

            KeyEvent.KEYCODE_DPAD_RIGHT to 79,
            KeyEvent.KEYCODE_DPAD_LEFT to 80,
            KeyEvent.KEYCODE_DPAD_DOWN to 81,
            KeyEvent.KEYCODE_DPAD_UP to 82,

            KeyEvent.KEYCODE_AT to 31,
            KeyEvent.KEYCODE_POUND to 32,
            KeyEvent.KEYCODE_STAR to 37










        )



    }
}