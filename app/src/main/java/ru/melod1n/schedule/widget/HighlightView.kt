package ru.melod1n.schedule.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem
import ru.melod1n.schedule.util.Util.px

class HighlightView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.dividerHorizontal, defStyleRes: Int = -1) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.dividerHorizontal)

    constructor(context: Context) : this(context, null)

    private var theme: ThemeItem? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    fun setTheme(theme: ThemeItem?) {
        this.theme = theme
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
        background = ColorDrawable(theme!!.colorHighlight)
        val params = layoutParams ?: return
        params.height = px(1f)
        layoutParams = params
    }

    init {
        if (theme == null) theme = currentTheme
        EventBus.getDefault().register(this)
    }
}