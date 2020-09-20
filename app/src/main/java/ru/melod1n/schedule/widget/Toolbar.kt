package ru.melod1n.schedule.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem
import ru.melod1n.schedule.util.ColorUtil.isLight

class Toolbar constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.toolbarStyle) : Toolbar(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.toolbarStyle)

    constructor(context: Context) : this(context, null)

    private var theme: ThemeItem? = null
    private var listenUpdates = false
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    fun setTheme(theme: ThemeItem?) {
        this.theme = theme
        init()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceive(info: EventInfo<ThemeItem?>) {
        val key = info.key
        if (EventInfo.KEY_THEME_UPDATE == key) {
            theme = info.data
            init()
            invalidate()
        }
    }

    fun init() {
        val colorPrimary = theme!!.colorPrimary
        val light = isLight(colorPrimary)
        val subtitleColor = if (light) Color.GRAY else Color.LTGRAY
        setBackgroundColor(colorPrimary)
        setTitleTextColor(ThemeEngine.colorMain)
        setSubtitleTextColor(subtitleColor)
        popupTheme = if (theme!!.isDark) R.style.ThemeOverlay_MaterialComponents_Dark else R.style.ThemeOverlay_MaterialComponents_Light
        if (navigationIcon != null) {
            navigationIcon!!.setTint(ThemeEngine.colorMain)
        }
        if (overflowIcon != null) {
            overflowIcon!!.setTint(ThemeEngine.colorMain)
        }
        setMenuIconsColor()
        var toolbarTitle: TextView? = null
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is TextView) {
                toolbarTitle = child
                break
            }
        }
        toolbarTitle?.setTypeface(ResourcesCompat.getFont(context, R.font.google_sans_regular), Typeface.BOLD)
    }

    override fun inflateMenu(resId: Int) {
        super.inflateMenu(resId)
        setMenuIconsColor()
    }

    private fun setMenuIconsColor() {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.icon != null) {
                item.icon.setTint(ThemeEngine.colorMain)
            }
        }
    }

    companion object {
        private const val TAG = "schedule.widget.Toolbar"
    }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Toolbar, 0, 0)
        if (a.hasValue(R.styleable.Toolbar_listenThemeUpdates)) {
            listenUpdates = a.getBoolean(R.styleable.Toolbar_listenThemeUpdates, false)
            if (listenUpdates) {
                EventBus.getDefault().register(this)
            } else {
                EventBus.getDefault().unregister(this)
            }
        }
        if (theme == null) theme = currentTheme
    }
}