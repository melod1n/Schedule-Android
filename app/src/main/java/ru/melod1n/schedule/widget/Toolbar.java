package ru.melod1n.schedule.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;
import ru.melod1n.schedule.util.Keys;

public class Toolbar extends androidx.appcompat.widget.Toolbar {

    private static final String TAG = "schedule.widget.Toolbar";

    private int titleColor;

    private ThemeItem theme;

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
            if (a.getBoolean(R.styleable.Toolbar_listenThemeUpdates, false)) {
                if (!EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().register(this);
            } else {
                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);
            }
        }

        if (theme == null) theme = ThemeEngine.getCurrentTheme();
        init();
    }

    public void setTheme(ThemeItem theme) {
        this.theme = theme;
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(@NonNull Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            setTheme((ThemeItem) data[1]);
        }
    }

    public void init() {
        int colorPrimary = theme.getColorPrimary();

        boolean light = ColorUtil.isLight(colorPrimary);

        titleColor = light ? Color.BLACK : Color.WHITE;

        int subtitleColor = light ? Color.GRAY : Color.LTGRAY;

        setBackgroundColor(colorPrimary);
        setTitleTextColor(titleColor);
        setSubtitleTextColor(subtitleColor);
        setPopupTheme(theme.isDark() ? R.style.ThemeOverlay_MaterialComponents_Dark : R.style.ThemeOverlay_MaterialComponents_Light);

        if (getNavigationIcon() != null) {
            getNavigationIcon().setTint(titleColor);
        }

        if (getOverflowIcon() != null) {
            getOverflowIcon().setTint(titleColor);
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
            toolbarTitle.setTypeface(ResourcesCompat.getFont(getContext(), theme.isMd2() ? R.font.ps_regular : R.font.roboto_regular), Typeface.BOLD);
        }
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
        for (int i = 0; i < getMenu().size(); i++) {
            MenuItem item = getMenu().getItem(i);

            if (item.getIcon() != null) {
                item.getIcon().setTint(titleColor);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
    }
}
