package ru.melod1n.schedule.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;

public class Toolbar extends androidx.appcompat.widget.Toolbar {

    private static final String TAG = "schedule.widget.Toolbar";

    private ThemeItem theme;

    private boolean listenUpdates;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Toolbar, 0, 0);

        if (a.hasValue(R.styleable.Toolbar_listenThemeUpdates)) {
            listenUpdates = a.getBoolean(R.styleable.Toolbar_listenThemeUpdates, false);
            if (listenUpdates) {
                EventBus.getDefault().register(this);
            } else {
                EventBus.getDefault().unregister(this);
            }
        }

        if (theme == null) theme = ThemeEngine.getCurrentTheme();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    public void setTheme(ThemeItem theme) {
        this.theme = theme;
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(@NotNull EventInfo<ThemeItem> info) {
        String key = info.getKey();

        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = info.getData();
            init();
            invalidate();
        }
    }

    public void init() {
        int colorPrimary = theme.getColorPrimary();

        boolean light = ColorUtil.isLight(colorPrimary);

        int subtitleColor = light ? Color.GRAY : Color.LTGRAY;

        setBackgroundColor(colorPrimary);
        setTitleTextColor(ThemeEngine.getColorMain());
        setSubtitleTextColor(subtitleColor);
        setPopupTheme(theme.isDark() ? R.style.ThemeOverlay_MaterialComponents_Dark : R.style.ThemeOverlay_MaterialComponents_Light);

        if (getNavigationIcon() != null) {
            getNavigationIcon().setTint(ThemeEngine.getColorMain());
        }

        if (getOverflowIcon() != null) {
            getOverflowIcon().setTint(ThemeEngine.getColorMain());
        }

        setMenuIconsColor();

        TextView toolbarTitle = null;
        for (int i = 0; i < getChildCount(); ++i) {
            View child = getChildAt(i);
            if (child instanceof TextView) {
                toolbarTitle = (TextView) child;
                break;
            }
        }

        if (toolbarTitle != null) {
            toolbarTitle.setTypeface(ResourcesCompat.getFont(getContext(), R.font.google_sans_regular), Typeface.BOLD);
        }
    }

    @Override
    public void inflateMenu(int resId) {
        super.inflateMenu(resId);
        setMenuIconsColor();
    }

    private void setMenuIconsColor() {
        for (int i = 0; i < getMenu().size(); i++) {
            MenuItem item = getMenu().getItem(i);

            if (item.getIcon() != null) {
                item.getIcon().setTint(ThemeEngine.getColorMain());
            }
        }
    }
}
