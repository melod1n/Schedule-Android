package ru.melod1n.schedule.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.items.ThemeItem;

public class PreferenceCheckBox extends CheckBoxPreference {
    public PreferenceCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.checkBoxPreferenceStyle, android.R.attr.checkBoxPreferenceStyle));
    }

    public PreferenceCheckBox(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        ThemeItem theme = ThemeManager.getCurrentTheme();

        int colorAccent = theme.getColorAccent();
        int textColorSecondary = theme.getColorTextSecondary();

        AppCompatCheckBox checkBox = (AppCompatCheckBox) holder.findViewById(android.R.id.checkbox);

        int[][] checkBoxStates = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked}
        };

        int[] checkBoxColors = new int[]{
                colorAccent,
                theme.alphaColor(textColorSecondary, 0.7f)
        };

        checkBox.setSupportButtonTintList(new ColorStateList(checkBoxStates, checkBoxColors));
    }
}
