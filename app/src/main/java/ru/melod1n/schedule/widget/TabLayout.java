package ru.melod1n.schedule.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Keys;

public class TabLayout extends com.google.android.material.tabs.TabLayout {

    private ThemeItem theme;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tabStyle);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        theme = ThemeEngine.getCurrentTheme();
        init();
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onReceive(@NonNull Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) data[1];
            init();
            requestLayout();
        }
    }

    private void init() {
        setBackgroundColor(theme.getColorPrimary());
        setTabTextColors(theme.getColorTabsTextNormal(), theme.getColorTabsTextActive());
        setSelectedTabIndicatorColor(theme.getColorTabsIndicator());

        setTabIndicatorFullWidth(!theme.isMd2());
        setSelectedTabIndicator(theme.isMd2() ? R.drawable.tabs_indicator_round : R.drawable.tabs_indicator);

    }
}
