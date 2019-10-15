package ru.melod1n.schedule.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Keys;

public class PreferenceCheckBox extends CheckBoxPreference {

    private ThemeItem theme;
    private PreferenceViewHolder holder;

    public PreferenceCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.checkBoxPreferenceStyle, android.R.attr.checkBoxPreferenceStyle));

        if (theme == null) theme = ThemeEngine.getCurrentTheme();
        EventBus.getDefault().register(this);
    }

    public PreferenceCheckBox(Context context) {
        this(context, null);
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
        int colorAccent = theme.getColorAccent();
        int textColorSecondary = theme.getColorTextSecondary();

        AppCompatCheckBox checkBox = (AppCompatCheckBox) holder.findViewById(android.R.id.checkbox);

        if (checkBox == null) {
            return;
        }

        int[][] checkBoxStates = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked}
        };

        int[] checkBoxColors = new int[]{
                colorAccent,
                theme.alphaColor(textColorSecondary, 0.7f)
        };

        checkBox.setSupportButtonTintList(new ColorStateList(checkBoxStates, checkBoxColors));

        final TextView titleView = (TextView) holder.findViewById(android.R.id.title);

        if (titleView == null) {
            return;
        }

        titleView.setTextColor(theme.getColorTextPrimary());

        final TextView summaryView = (TextView) holder.findViewById(android.R.id.summary);

        if (summaryView == null) {
            return;
        }

        summaryView.setTextColor(theme.getColorTextSecondary());
    }
}
