package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.common.TimeManager;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Util;

public class NoItemsView extends TextView {

    private ThemeItem theme;

    public NoItemsView(Context context) {
        this(context, null);
    }

    public NoItemsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoItemsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        EventBus.getDefault().register(this);

        theme = ThemeEngine.getCurrentTheme();
        init();

        TimeManager.addOnHourChangeListener(currentHour -> initIcon());
    }

    private void initIcon() {
        Drawable icon = null;

        if (TimeManager.isMorning() || TimeManager.isAfternoon()) {
            icon = getContext().getDrawable(R.drawable.ic_vector_sun);
        } else if (TimeManager.isEvening() || TimeManager.isNight()) {
            icon = getContext().getDrawable(R.drawable.ic_vector_moon);
        }

        setCompoundDrawablePadding(Util.px(8));
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, icon, null, null);
    }

    private void init() {
        int textColor = theme.getColorTextSecondary();
        setTextColor(textColor);

        initIcon();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo<ThemeItem> info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = info.getData();
            init();
        }
    }
}
