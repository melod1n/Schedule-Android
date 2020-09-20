package ru.melod1n.schedule.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import org.apache.commons.lang3.StringUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem
import java.util.*

class Button constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.materialButtonStyle) : MaterialButton(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.materialButtonStyle)

    constructor(context: Context) : this(context, null)

    private var theme: ThemeItem? = null
    private var style = 0

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceive(info: EventInfo<ThemeItem?>) {
        val key = info.key
        if (EventInfo.KEY_THEME_UPDATE == key) {
            theme = info.data
            init()
        }
    }

    private fun init() {
        val colorAccent = theme!!.colorAccent
        val textColorPrimaryInverse = theme!!.colorTextPrimaryInverse
        val textColorSecondary = theme!!.colorTextSecondary

        typeface = ResourcesCompat.getFont(context, R.font.google_sans_medium)

        text = StringUtils.capitalize(text.toString().toLowerCase(Locale.getDefault()))

        if (style == STYLE_DEFAULT) {
            setTextColor(textColorPrimaryInverse)
            backgroundTintList = ColorStateList.valueOf(colorAccent)
        } else if (style == STYLE_OUTLINE) {
            setTextColor(colorAccent)
            strokeColor = ColorStateList.valueOf(theme!!.alphaColor(textColorSecondary, 0.3f))
            rippleColor = ColorStateList.valueOf(colorAccent)
            backgroundTintList = ColorStateList.valueOf(0)
        }
    }

    companion object {
        const val STYLE_DEFAULT = 0
        const val STYLE_OUTLINE = 1
    }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Button, 0, 0)
        if (a.hasValue(R.styleable.Button_style)) {
            style = a.getInt(R.styleable.Button_style, 0)
        }
        EventBus.getDefault().register(this)
        if (theme == null) theme = currentTheme
    }
}