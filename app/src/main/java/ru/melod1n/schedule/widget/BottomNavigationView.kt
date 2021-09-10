package ru.melod1n.schedule.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem

class BottomNavigationView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.bottomNavigationStyle) : BottomNavigationView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.bottomNavigationStyle)

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
        val bottomNavigationViewStates = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked))
        val bottomNavigationViewColors = intArrayOf(
                theme!!.colorBottomBarIconsActive,
                theme!!.colorBottomBarIconsNormal
        )
        val colorStateList = ColorStateList(bottomNavigationViewStates, bottomNavigationViewColors)
        itemIconTintList = colorStateList
        itemTextColor = colorStateList
        setBackgroundColor(theme!!.colorBottomBar)
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        theme = currentTheme
    }
}