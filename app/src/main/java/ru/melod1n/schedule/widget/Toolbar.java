package ru.melod1n.schedule.widget;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;

public class Toolbar extends androidx.appcompat.widget.Toolbar {

    private static final String TAG = "schedule.widget.Toolbar";

    private int titleColor;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ThemeItem theme = ThemeEngine.getCurrentTheme();

        int colorPrimary = theme.getColorPrimary();
        int titlePrimary;

        titleColor = titlePrimary = ColorUtil.isLight(colorPrimary) ? Color.BLACK : Color.WHITE;
        int titleSecondary = ColorUtil.isLight(colorPrimary) ? Color.LTGRAY : Color.DKGRAY;

        setBackgroundColor(colorPrimary);
        setTitleTextColor(titlePrimary);
        setSubtitleTextColor(titleSecondary);
        setPopupTheme(theme.isDark() ? R.style.ThemeOverlay_MaterialComponents_Dark : R.style.ThemeOverlay_MaterialComponents_Light);

        setMenuIconsColor();
    }

    public int getTitleColor() {
        return titleColor;
    }

    @Override
    public void inflateMenu(int resId) {
        super.inflateMenu(resId);
        setMenuIconsColor();
    }

    private void setMenuIconsColor() {
        if (getNavigationIcon() != null) {
            getNavigationIcon().setTint(titleColor);
        }

        if (getOverflowIcon() != null) {
            getOverflowIcon().setTint(titleColor);
        }

        for (int i = 0; i < getMenu().size(); i++) {
            MenuItem item = getMenu().getItem(i);

            if (item.getIcon() != null) {
                item.getIcon().setTint(titleColor);
            }
        }
    }
}
