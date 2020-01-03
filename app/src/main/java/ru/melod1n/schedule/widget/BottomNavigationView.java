package ru.melod1n.schedule.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class BottomNavigationView extends com.google.android.material.bottomnavigation.BottomNavigationView {

    private ThemeItem theme;

    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.bottomNavigationStyle);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        theme = ThemeEngine.getCurrentTheme();
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) info.getData();
            init();
        }
    }

    private void init() {
        int[][] bottomNavigationViewStates = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked}
        };

        int[] bottomNavigationViewColors = new int[]{
                theme.getColorBottomBarIconsActive(),
                theme.getColorBottomBarIconsNormal()
        };

        ColorStateList colorStateList = new ColorStateList(bottomNavigationViewStates, bottomNavigationViewColors);

        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setBackgroundColor(theme.getColorBottomBar());
    }
}
