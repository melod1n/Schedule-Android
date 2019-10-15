package ru.melod1n.schedule.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.ListPreference;

public class PreferenceList extends ListPreference {
    public PreferenceList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PreferenceList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferenceList(Context context) {
        super(context);
    }
}
