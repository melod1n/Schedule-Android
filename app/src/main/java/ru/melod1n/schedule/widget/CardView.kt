package ru.melod1n.schedule.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem

class CardView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.materialCardViewStyle) : MaterialCardView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.materialCardViewStyle)

    constructor(context: Context) : this(context, null)

    private var theme: ThemeItem? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    private fun init() {
        setCardBackgroundColor(theme!!.colorSurface)
        useCompatPadding = true
        radius = 16f
        strokeColor = theme!!.colorHighlight
        strokeWidth = 2
        cardElevation = 0f
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceive(info: EventInfo<ThemeItem?>) {
        val key = info.key
        if (EventInfo.KEY_THEME_UPDATE == key) {
            theme = info.data
            init()
        }
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        if (theme == null) theme = currentTheme
    }
}