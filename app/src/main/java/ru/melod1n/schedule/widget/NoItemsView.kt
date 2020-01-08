package ru.melod1n.schedule.widget

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.common.TimeManager
import ru.melod1n.schedule.items.ThemeItem
import ru.melod1n.schedule.util.Util

class NoItemsView(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var theme: ThemeItem

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private fun initIcon() {
        val icon = ContextCompat.getDrawable(context, if (TimeManager.isMorning() || TimeManager.isAfternoon()) R.drawable.ic_vector_sun else R.drawable.ic_vector_moon)

        compoundDrawablePadding = Util.px(8f)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, icon, null, null)
    }

    private fun init() {
        val textColor = theme.colorTextSecondary

        setTextColor(textColor)
        initIcon()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceive(info: EventInfo<ThemeItem>) {
        val key = info.key
        if (EventInfo.KEY_THEME_UPDATE == key) {
            theme = info.data
            init()
        }
    }

    init {
        EventBus.getDefault().register(this)
        theme = ThemeEngine.getCurrentTheme()
        init()
        TimeManager.addOnHourChangeListener { Handler().post { initIcon() } }
    }
}