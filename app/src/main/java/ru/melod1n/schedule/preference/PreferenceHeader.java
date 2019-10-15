package ru.melod1n.schedule.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Keys;

public class PreferenceHeader extends PreferenceCategory {

    private ThemeItem theme;
    private PreferenceViewHolder holder;

    public PreferenceHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceHeader(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.preferenceCategoryStyle, android.R.attr.preferenceCategoryStyle));
    }

    public PreferenceHeader(Context context) {
        this(context, null);
    }

    public PreferenceHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (theme == null) theme = ThemeEngine.getCurrentTheme();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(@NonNull Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) data[1];
            init();
        }
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        if (this.holder == null) this.holder = holder;
        if (theme == null) theme = ThemeEngine.getCurrentTheme();

        init();
    }

    private void init() {
        final TextView titleView = (TextView) holder.findViewById(android.R.id.title);

        if (titleView == null) {
            return;
        }

        titleView.setTextColor(theme.getColorAccent());
    }
}
