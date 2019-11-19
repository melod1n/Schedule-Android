package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.common.TimeManager;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Keys;
import ru.melod1n.schedule.util.Util;

public class NoItemsView extends AppCompatTextView {

    private ThemeItem theme;

    public NoItemsView(Context context) {
        this(context, null);
    }

    public NoItemsView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public NoItemsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!EventBus.getDefault().isRegistered(this))
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
    public void onReceive(Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) data[1];
            init();
        }
    }
}
