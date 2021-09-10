package ru.melod1n.schedule.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.TintTypedArray
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.model.ThemeItem

@SuppressLint("RestrictedApi", "AppCompatCustomView")
class TextPlain constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle) : TextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle)

    constructor(context: Context) : this(context, null)

    private var theme: ThemeItem
    private var colorDef = 0

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceive(info: EventInfo<ThemeItem>) {
        val key = info.key

        if (EventInfo.KEY_THEME_UPDATE == key) {
            theme = info.data
            init()
        }
    }

    private fun init() {
        val textColor = getColor(colorDef)
        setTextColor(textColor)

        val drawables = compoundDrawables

        if (drawables.isNotEmpty()) {
            for (drawable in drawables) {
                drawable?.setTint(textColor)
            }
        }
    }

    private fun getColor(def: Int): Int {
        return when (def) {
            0 -> theme.colorTextPrimary
            1 -> theme.colorTextSecondary
            2 -> theme.colorAccent
            else -> theme.colorTextSecondary
        }
    }

    init {
        val a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.TextPlain, defStyleAttr, 0)
        colorDef = if (a.hasValue(R.styleable.TextPlain_fontColor)) {
            a.getInt(R.styleable.TextPlain_fontColor, 1)
        } else {
            1
        }

        theme = ThemeEngine.currentTheme!!

        init()

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }
}