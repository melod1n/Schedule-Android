package ru.melod1n.schedule.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem

class FloatingActionButton constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = com.google.android.material.R.attr.floatingActionButtonStyle) : FloatingActionButton(context!!, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, com.google.android.material.R.attr.floatingActionButtonStyle)

    constructor(context: Context) : this(context, null)

    private var theme: ThemeItem?
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
        supportBackgroundTintList = ColorStateList.valueOf(theme!!.fabColor)
        if (drawable != null) {
            drawable.setTint(theme!!.fabIconColor)
        }
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        theme = currentTheme
    }
}