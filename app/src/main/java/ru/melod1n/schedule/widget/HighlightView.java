package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

        if (theme == null) theme = ThemeEngine.getCurrentTheme();

        EventBus.getDefault().register(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo<ThemeItem> info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = info.getData();
            init();
        }
    }

    private void init() {
        setBackground(new ColorDrawable(theme.getColorHighlight()));

        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) return;

        params.height = Util.px(1);

        setLayoutParams(params);
    }
}
