package ru.melod1n.schedule.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.ThemeEngine.currentTheme

class TextArea constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextStyle) : TextInputEditText(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)

    constructor(context: Context) : this(context, null)

    private var cursorDrawable: Drawable? = null

    private fun init() {
        val themeItem = currentTheme
        cursorDrawable = ContextCompat.getDrawable(context, R.drawable.edit_text_cursor)
        val colorAccent = themeItem!!.colorAccent

        //TODO: дописать
    }

    init {
        init()
    }
}