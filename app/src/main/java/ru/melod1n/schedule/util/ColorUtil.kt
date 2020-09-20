package ru.melod1n.schedule.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import java.util.regex.Pattern
import kotlin.math.*

object ColorUtil {

    fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.75f

        return Color.HSVToColor(hsv)
    }

    @FloatRange(from = 0.0, to = 1.0)
    fun calculateLuminance(@ColorInt color: Int): Double {
        var red = Color.red(color) / 255.0
        red = if (red < 0.03928) red / 12.92 else ((red + 0.055) / 1.055).pow(2.4)
        var green = Color.green(color) / 255.0
        green = if (green < 0.03928) green / 12.92 else ((green + 0.055) / 1.055).pow(2.4)
        var blue = Color.blue(color) / 255.0
        blue = if (blue < 0.03928) blue / 12.92 else ((blue + 0.055) / 1.055).pow(2.4)
        return 0.2126 * red + 0.7152 * green + 0.0722 * blue
    }

    @JvmStatic
    fun isLight(@ColorInt color: Int): Boolean {
        return calculateLuminance(color) >= 0.5
    }

    fun isDark(@ColorInt color: Int): Boolean {
        return calculateLuminance(color) < 0.5
    }

    fun isValidHexColor(color: String): Boolean {
        return Pattern.matches("^#?([a-f0-9]{6}|[a-f0-9]{3})$", color)
    }

    @ColorInt
    fun createRandomColor(): Int {
        val red = (Math.random() * 256).toInt()
        val green = (Math.random() * 256).toInt()
        val blue = (Math.random() * 256).toInt()
        return Color.rgb(red, green, blue)
    }
}