package ru.melod1n.schedule.widget.horizontalcolorpicker

import androidx.annotation.ColorInt

interface OnSelectedColorListener {
    fun onSelectedColor(position: Int, @ColorInt color: Int)
}