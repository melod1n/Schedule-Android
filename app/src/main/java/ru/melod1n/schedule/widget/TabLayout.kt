package ru.melod1n.schedule.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem

class TabLayout constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.tabStyle) : TabLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.tabStyle)

    constructor(context: Context) : this(context, null)

    private var theme: ThemeItem? = null

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
        if (theme == null) theme = currentTheme
        setBackgroundColor(theme!!.colorPrimary)
        setTabTextColors(theme!!.colorTabsTextNormal, theme!!.colorTabsTextActive)
        setSelectedTabIndicatorColor(theme!!.colorAccent)
    } //    void changeFontInViewGroup(ViewGroup viewGroup) {

    //        for (int i = 0; i < viewGroup.getChildCount(); i++) {
    //            View child = viewGroup.getChildAt(i);
    //            if (TextView.class.isAssignableFrom(child.getClass())) {
    //                TextView text = (TextView) child;
    //                if (theme.isMd2()) {
    //                    FontHelper.applyFont(this, FontHelper.Font.PS_MEDIUM);
    //                    text.setText(StringUtils.capitalize(text.getText().toString().toLowerCase()));
    //                } else {
    //                    FontHelper.applyFont(this, FontHelper.Font.ROBOTO_REGULAR);
    //                    text.setText(text.getText().toString().toUpperCase());
    //                }
    //            } else if (ViewGroup.class.isAssignableFrom(child.getClass())) {
    //                changeFontInViewGroup((ViewGroup) viewGroup.getChildAt(i));
    //            }
    //        }
    //    }
    init {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        if (theme == null) theme = currentTheme
    }
}