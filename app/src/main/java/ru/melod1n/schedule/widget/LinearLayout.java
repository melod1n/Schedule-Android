package ru.melod1n.schedule.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class LinearLayout extends android.widget.LinearLayout {

    private ThemeItem theme;

    public LinearLayout(Context context) {
        this(context, null);
    }

    public LinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

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
        setBackgroundColor(theme.getColorBackground());
    }
}
