package ru.melod1n.schedule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Util;

public class HighlightView extends View {

    private ThemeItem theme;

    private int direction;

    public static final int DIRECTION_BOTTOM = 0;
    public static final int DIRECTION_TOP = 1;

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

        boolean listenUpdates = true;

        if (a.hasValue(R.styleable.HighlightView_listenUpdates)) {
            listenUpdates = a.getBoolean(R.styleable.HighlightView_listenUpdates, true);
        }

        if (a.hasValue(R.styleable.HighlightView_direction)) {
            direction = a.getInt(R.styleable.HighlightView_direction, 0);
        }

        a.recycle();

        if (listenUpdates) {
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        } else {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }

        theme = ThemeEngine.getCurrentTheme();
        init();
    }

    public void setTheme(ThemeItem theme) {
        this.theme = theme;
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo<ThemeItem> info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = info.getData();
            init();
        }
    }

    private void init() {
        Drawable drawable = getContext().getDrawable(R.drawable.highlight_view_divider);

        if (drawable != null) {
            drawable.setTint(theme.getColorHighlight());
        }

        setBackground(drawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = Util.px(1);

        setLayoutParams(params);
    }
}
