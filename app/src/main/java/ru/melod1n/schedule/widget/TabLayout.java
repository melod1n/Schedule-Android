package ru.melod1n.schedule.widget;

import android.content.Context;
import android.util.AttributeSet;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.items.ThemeItem;

public class TabLayout extends com.google.android.material.tabs.TabLayout {
    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tabStyle);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ThemeItem theme = ThemeManager.getCurrentTheme();

        int colorPrimary = theme.getColorPrimary();
        int colorAccent = theme.getColorAccent();
        int colorTabsText = theme.getColorTabsText();

        setBackgroundColor(colorPrimary);
        setTabTextColors(colorTabsText, colorAccent);
        setSelectedTabIndicatorColor(colorAccent);
    }
}
