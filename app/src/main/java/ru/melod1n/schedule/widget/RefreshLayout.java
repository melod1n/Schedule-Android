package ru.melod1n.schedule.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class RefreshLayout extends SwipeRefreshLayout {
    public RefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ThemeItem theme = ThemeEngine.getCurrentTheme();

        int arrowColor = theme.getColorAccent();
        int backgroundColor = theme.getColorSurface();

        setColorSchemeColors(arrowColor);
        setProgressBackgroundColorSchemeColor(backgroundColor);

    }

}
