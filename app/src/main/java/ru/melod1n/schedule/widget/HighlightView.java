package ru.melod1n.schedule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Keys;

public class HighlightView extends View {

    private ThemeItem theme;

    private int direction;

    public static final int DIRECTION_TOP = 0;
    public static final int DIRECTION_BOTTOM = 1;

    public HighlightView(Context context) {
        this(context, null);
    }

    public HighlightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.dividerHorizontal);
    }

    public HighlightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public HighlightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HighlightView, 0, 0);

        if (a.hasValue(R.styleable.HighlightView_direction)) {
            direction = a.getInt(R.styleable.HighlightView_direction, 1);
        }

        a.recycle();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        theme = ThemeEngine.getCurrentTheme();
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(@NonNull Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) data[1];
            init();
            requestLayout();
        }
    }

    private void init() {
        Drawable drawable = getContext().getDrawable(!theme.isMd2() ? (direction == DIRECTION_BOTTOM ? R.drawable.highlight_view_shadow_bottom : R.drawable.highlight_view_shadow_top) : R.drawable.highlight_view_divider);

        if (theme.isMd2() && drawable != null) {
            drawable.setTint(theme.getColorHighlight());
        }

        setBackground(drawable);
    }
}
