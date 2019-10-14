package ru.melod1n.schedule.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class BottomNavigationView extends com.google.android.material.bottomnavigation.BottomNavigationView {

    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.bottomNavigationStyle);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ThemeItem theme = ThemeEngine.getCurrentTheme();

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
