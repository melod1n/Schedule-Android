package ru.melod1n.schedule.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem
import ru.melod1n.schedule.util.ViewUtil

abstract class BaseActivity : AppCompatActivity() {

    private var contentView: View? = null

    protected var theme: ThemeItem? = null
    protected var created = false

    override fun setContentView(contentView: View) {
        this.contentView = contentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        init()

        super.onCreate(savedInstanceState)

        created = true
    }

    protected open fun init() {
        if (theme == null) theme = currentTheme

        setTheme(if (theme!!.isDark) R.style.AppTheme_Dark else R.style.AppTheme)

        ViewUtil.applyWindowStyles(window)

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onReceive(info: EventInfo<Any>) {
        val key = info.key

        if (EventInfo.KEY_THEME_UPDATE == key) {
            theme = info.data as ThemeItem
            init()
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)

        created = false

        super.onDestroy()
    }

    protected fun applyBackground() {
        contentView?.setBackgroundColor(currentTheme!!.colorBackground)
    }
}