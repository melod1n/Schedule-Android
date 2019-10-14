package ru.melod1n.schedule.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;

public class PreferenceHeader extends PreferenceCategory {

    public PreferenceHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceHeader(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.preferenceCategoryStyle,
                android.R.attr.preferenceCategoryStyle));
    }

    public PreferenceHeader(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        final TextView titleView = (TextView) holder.findViewById(android.R.id.title);

        if (titleView == null) {
            return;
        }

        titleView.setTextColor(ThemeEngine.getCurrentTheme().getColorAccent());
    }
}
