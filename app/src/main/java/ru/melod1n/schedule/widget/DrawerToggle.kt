package ru.melod1n.schedule.widget

import android.app.Activity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem

class DrawerToggle(activity: Activity, drawerLayout: DrawerLayout?, toolbar: Toolbar?, openDrawerContentDescRes: Int, closeDrawerContentDescRes: Int) : ActionBarDrawerToggle(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes) {

    private var theme: ThemeItem? = null
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceive(info: EventInfo<ThemeItem?>) {
        val key = info.key
        if (EventInfo.KEY_THEME_UPDATE == key) {
            theme = info.data
            init()
        }
    }

    private fun init() {
        drawerArrowDrawable.color = ThemeEngine.colorMain
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        if (theme == null) theme = currentTheme
        init()
    }
}