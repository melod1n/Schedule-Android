package ru.melod1n.schedule.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem

class NavigationDrawer constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.navigationViewStyle) : NavigationView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, R.attr.navigationViewStyle)

    constructor(context: Context) : this(context, null, -1)

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
        val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked))
        val textColors = intArrayOf(
                theme!!.colorDrawerTextActive,
                theme!!.colorDrawerTextNormal
        )
        val iconColors = intArrayOf(
                theme!!.colorDrawerIconActive,
                theme!!.colorDrawerIconNormal
        )
        setBackgroundColor(theme!!.colorDrawer)
        itemTextColor = ColorStateList(states, textColors)
        itemIconTintList = ColorStateList(states, iconColors)
        val header = getHeaderView(0) ?: return
        header.setBackgroundColor(theme!!.colorDrawerHeaderBackground)
        val title = header.findViewById<TextView>(R.id.drawerTitle)
        val subtitle = header.findViewById<TextView>(R.id.drawerSubtitle)
        title.setTextColor(theme!!.colorDrawerHeaderTitle)
        subtitle.setTextColor(theme!!.colorDrawerHeaderSubtitle)
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        theme = currentTheme
    }
}