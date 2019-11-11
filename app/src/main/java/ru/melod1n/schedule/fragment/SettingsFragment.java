package ru.melod1n.schedule.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.SettingsActivity;
import ru.melod1n.schedule.ThemesActivity;
import ru.melod1n.schedule.common.Engine;
import ru.melod1n.schedule.common.ThemeEngine;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {


    //Categories
    private static final String CATEGORY_APPEARANCE = "appearance";
    private static final String CATEGORY_SCHEDULE = "schedule";
    private static final String CATEGORY_DEBUG = "debug";

    public static final String KEY_THEME = "theme";
    private static final String KEY_THEME_MANAGER = "theme_manager";
    static final String KEY_SELECT_CURRENT_DAY = "select_current_day";

    public static final String KEY_SHOW_DATE = "show_date_instead_interim";

    public static final String KEY_SHOW_ERROR = "show_error";

    private int currentPreferenceLayout;

    @Override
    public void onCreatePreferences(Bundle p1, String p2) {
        if (getArguments() != null) {
            currentPreferenceLayout = getArguments().getInt("layout_id", R.xml.fragment_settings);
        } else {
            currentPreferenceLayout = R.xml.fragment_settings;
        }

        init();
    }

    private void init() {
        setTitle();
        setPreferencesFromResource(currentPreferenceLayout, null);

        PreferenceScreen appearance = findPreference(CATEGORY_APPEARANCE);

        if (appearance != null) {
            appearance.setOnPreferenceClickListener(this::changeRootLayout);
        }

        Preference theme = findPreference(KEY_THEME);

        if (theme != null) {
            theme.setSummary(getString(R.string.theme_summary, ThemeEngine.getCurrentTheme().getTitle(), ThemeEngine.getCurrentTheme().getAuthor()));
        }

        Preference themeManager = findPreference(KEY_THEME_MANAGER);

        if (themeManager != null) {
            themeManager.setOnPreferenceClickListener(this);
        }

        Preference schedule = findPreference(CATEGORY_SCHEDULE);

        if (schedule != null) {
            schedule.setOnPreferenceClickListener(this::changeRootLayout);
        }

        Preference showDate = findPreference(KEY_SHOW_DATE);

        if (showDate != null) {
            showDate.setOnPreferenceChangeListener(this);
        }

        Preference debug = findPreference(CATEGORY_DEBUG);

        if (debug != null) {
            debug.setOnPreferenceClickListener(this::changeRootLayout);
        }

        applyTintInPreferenceScreen(getPreferenceScreen());
    }

    private void setTitle() {
        int title = R.string.settings;
        switch (currentPreferenceLayout) {
            case R.xml.fragment_settings:
                title = R.string.settings;
                break;
            case R.xml.category_appearance:
                title = R.string.pref_appearance_title;
                break;
            case R.xml.category_schedule:
                title = R.string.pref_schedule_title;
                break;
            case R.xml.category_debug:
                title = R.string.pref_debug_title;
                break;
        }

        if (getActivity() != null)
            getActivity().setTitle(title);
    }

    private void applyTintInPreferenceScreen(@NonNull PreferenceScreen rootScreen) {
        if (rootScreen.getPreferenceCount() > 0) {
            for (int i = 0; i < rootScreen.getPreferenceCount(); i++) {
                Preference preference = rootScreen.getPreference(i);
                tintPreference(preference);
            }
        }
    }

    private void tintPreference(@NonNull Preference preference) {
        if (preference.getIcon() != null && getContext() != null) {
            preference.getIcon().setTint(getContext().getColor(ThemeEngine.getCurrentTheme().isDark() ? R.color.dark_accent : R.color.accent));
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case KEY_THEME_MANAGER:
                startThemesActivity();
                break;
        }
        return true;
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case KEY_SHOW_DATE:
                sendChangeShowDateEvent((boolean) newValue);
                return true;
        }
        return false;
    }

    private void sendChangeShowDateEvent(boolean newValue) {
        Engine.sendEvent(KEY_SHOW_DATE, newValue);
    }

    private boolean changeRootLayout(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case CATEGORY_APPEARANCE:
                currentPreferenceLayout = R.xml.category_appearance;
                break;
            case CATEGORY_DEBUG:
                currentPreferenceLayout = R.xml.category_debug;
                break;
            case CATEGORY_SCHEDULE:
                currentPreferenceLayout = R.xml.category_schedule;
                break;
            default:
                currentPreferenceLayout = R.xml.fragment_settings;
                break;
        }

        init();
        return true;
    }

    private void startThemesActivity() {
        startActivity(new Intent(getActivity(), ThemesActivity.class));
    }

    public boolean onBackPressed() {
        if (currentPreferenceLayout == R.xml.fragment_settings) {
            return true;
        } else {
            currentPreferenceLayout = R.xml.fragment_settings;
            init();

            return false;
        }
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null) {
            ((SettingsActivity) getActivity()).setFragmentElement(currentPreferenceLayout);
        }
        super.onDestroy();
    }
}
