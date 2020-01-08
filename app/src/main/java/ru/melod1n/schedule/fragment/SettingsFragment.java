package ru.melod1n.schedule.fragment;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.activity.MainActivity;
import ru.melod1n.schedule.activity.SettingsActivity;
import ru.melod1n.schedule.activity.ThemesActivity;
import ru.melod1n.schedule.app.AlertBuilder;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.common.Engine;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.common.TimeManager;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.widget.TextArea;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {


    //Categories
    private static final String CATEGORY_ACCOUNT = "account";
    private static final String CATEGORY_APPEARANCE = "appearance";
    private static final String CATEGORY_SCHEDULE = "schedule";
    private static final String CATEGORY_DEBUG = "debug";

    public static final String KEY_USER_NAME = "user_name";

    public static final String KEY_THEME = "theme";
    private static final String KEY_THEME_MANAGER = "theme_manager";
    public static final String KEY_AUTO_SWITCH_THEME = "auto_switch_theme";
    public static final String KEY_DAY_TIME_THEME = "day_time_theme";
    public static final String KEY_NIGHT_TIME_THEME = "night_time_theme";
    static final String KEY_SELECT_CURRENT_DAY = "select_current_day";

    public static final String KEY_SHOW_DATE = "show_date_instead_interim";

    public static final String KEY_SHOW_ERROR = "show_error";
    public static final String KEY_SET_MORNING_TIME = "set_morning_time";
    public static final String KEY_SET_EVENING_TIME = "set_evening_time";

    private int currentPreferenceLayout;

    private Preference dayTimeTheme, nightTimeTheme, themeManager;

    @Override
    public void onCreatePreferences(Bundle p1, String p2) {
        if (getArguments() != null) {
            currentPreferenceLayout = getArguments().getInt("layout_id", R.xml.fragment_settings);
        } else {
            currentPreferenceLayout = R.xml.fragment_settings;
        }

        init();

        TimeManager.addOnHourChangeListener(currentHour -> {
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> autoSwitchVisibility(null));
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            ((MainActivity) requireActivity()).prepareScreenSwipe(0);
        }
        super.onHiddenChanged(hidden);
    }

    private void init() {
        setTitle();
        setPreferencesFromResource(currentPreferenceLayout, null);

        PreferenceScreen account = findPreference(CATEGORY_ACCOUNT);

        if (account != null) {
            account.setOnPreferenceClickListener(this::changeRootLayout);
        }

        PreferenceScreen userName = findPreference(KEY_USER_NAME);

        if (userName != null) {
            userName.setOnPreferenceClickListener(this);

            userName.setSummary(AppGlobal.preferences.getString(KEY_USER_NAME, ""));
        }

        PreferenceScreen appearance = findPreference(CATEGORY_APPEARANCE);

        if (appearance != null) {
            appearance.setOnPreferenceClickListener(this::changeRootLayout);
        }

        Preference theme = findPreference(KEY_THEME);

        if (theme != null) {
            ThemeItem currentTheme = ThemeEngine.getCurrentTheme();

            if (StringUtils.isEmpty(currentTheme.getAuthor())) {
                theme.setSummary(currentTheme.getTitle());
            } else {
                theme.setSummary(getString(R.string.theme_summary, currentTheme.getTitle(), currentTheme.getAuthor()));
            }
        }

        themeManager = findPreference(KEY_THEME_MANAGER);

        if (themeManager != null) {
            themeManager.setOnPreferenceClickListener(this);
        }

        Preference autoSwitchTheme = findPreference(KEY_AUTO_SWITCH_THEME);

        if (autoSwitchTheme != null) {
            autoSwitchTheme.setOnPreferenceChangeListener(this);
        }

        dayTimeTheme = findPreference(KEY_DAY_TIME_THEME);

        if (dayTimeTheme != null) {
            dayTimeTheme.setOnPreferenceClickListener(this);
        }

        nightTimeTheme = findPreference(KEY_NIGHT_TIME_THEME);

        if (nightTimeTheme != null) {
            nightTimeTheme.setOnPreferenceClickListener(this);
        }

        autoSwitchVisibility(null);

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

        Preference setMorningTime = findPreference(KEY_SET_MORNING_TIME);

        if (setMorningTime != null) {
            setMorningTime.setOnPreferenceClickListener(this);
        }

        Preference setEveningTime = findPreference(KEY_SET_EVENING_TIME);

        if (setEveningTime != null) {
            setEveningTime.setOnPreferenceClickListener(this);
        }

        applyTintInPreferenceScreen(getPreferenceScreen());
    }

    private void setTitle() {
        int title = R.string.settings;

        switch (currentPreferenceLayout) {
            case R.xml.fragment_settings:
                title = R.string.settings;
                break;
            case R.xml.category_account:
                title = R.string.pref_account_title;
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

        requireActivity().setTitle(title);
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
            preference.getIcon().setTint(ContextCompat.getColor(getContext(), ThemeEngine.getCurrentTheme().isDark() ? R.color.dark_accent : R.color.accent));
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case KEY_DAY_TIME_THEME:
            case KEY_NIGHT_TIME_THEME:
            case KEY_THEME_MANAGER:
                startThemesActivity(preference);
                return true;
            case KEY_SET_MORNING_TIME:
                setMorningTime();
                return true;
            case KEY_SET_EVENING_TIME:
                setEveningTime();
                return true;
            case KEY_USER_NAME:
                showUserNameDialog();
                break;
        }

        return false;
    }

    private void showUserNameDialog() {
        AlertBuilder builder = new AlertBuilder(requireContext());
        builder.setTitle(R.string.pref_account_user_name_title);

        TextArea area = new TextArea(requireContext());
        area.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        area.setHint(R.string.pref_account_user_name_title);
        area.setText(AppGlobal.preferences.getString(KEY_USER_NAME, ""));
        area.requestFocus();

        builder.setCustomView(area);
        builder.setPositiveButton(R.string.change, v -> {
            String name;
            if (area.getText() == null || area.getText().toString().trim().isEmpty()) {
               name = getString(R.string.drawer_title_no_user);
            } else {
                name = area.getText().toString().trim();
            }


            if (TextUtils.isEmpty(name)) return;

            AppGlobal.preferences.edit().putString(KEY_USER_NAME, name).apply();

            findPreference(KEY_USER_NAME).setSummary(name);

            EventBus.getDefault().postSticky(new EventInfo<>(EventInfo.KEY_USER_NAME_UPDATE, name));
        });

        builder.setNegativeButton(android.R.string.cancel);
        builder.show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case KEY_SHOW_DATE:
                sendChangeShowDateEvent((boolean) newValue);
                return true;
            case KEY_AUTO_SWITCH_THEME:
                boolean autoTheme = (boolean) newValue;


                ThemeEngine.setAutoTheme(autoTheme);
                autoSwitchVisibility(autoTheme);

                if (autoTheme) {
                    if (TimeManager.isMorning() || TimeManager.isAfternoon()) {
                        if (!ThemeEngine.getCurrentTheme().equals(ThemeEngine.getDayTheme())) {
                            ThemeEngine.setCurrentTheme(ThemeEngine.getDayTheme().getId());
                        }
                    } else {
                        if (!ThemeEngine.getCurrentTheme().equals(ThemeEngine.getNightTheme())) {
                            ThemeEngine.setCurrentTheme(ThemeEngine.getNightTheme().getId());
                        }
                    }
                } else {
                    if (!ThemeEngine.getCurrentTheme().getId().toLowerCase().equals(ThemeEngine.getSelectedThemeKey())) {
                        ThemeEngine.setCurrentTheme(ThemeEngine.getSelectedThemeKey());
                    }
                }
                return true;
        }
        return false;
    }

    private void autoSwitchVisibility(Boolean b) {
        boolean visible = b != null ? b : AppGlobal.preferences.getBoolean(KEY_AUTO_SWITCH_THEME, false);

        if (themeManager != null) {
            themeManager.setVisible(!visible);
        }

        if (dayTimeTheme == null || nightTimeTheme == null) return;

        dayTimeTheme.setVisible(visible);
        nightTimeTheme.setVisible(visible);

        if (TimeManager.isMorning() || TimeManager.isAfternoon()) {
            dayTimeTheme.setSummary(R.string.pref_appearance_time_theme_current_theme);
        } else {
            dayTimeTheme.setSummary("");
        }

        if (TimeManager.isEvening() || TimeManager.isNight()) {
            nightTimeTheme.setSummary(R.string.pref_appearance_time_theme_current_theme);
        } else {
            nightTimeTheme.setSummary("");
        }
    }

    private void sendChangeShowDateEvent(boolean newValue) {
        Engine.sendEvent(new EventInfo<>(KEY_SHOW_DATE, newValue));
    }

    private boolean changeRootLayout(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case CATEGORY_ACCOUNT:
                currentPreferenceLayout = R.xml.category_account;
                break;
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

    private void startThemesActivity(@NonNull Preference preference) {
        int request = -1;

        switch (preference.getKey()) {
            case KEY_THEME_MANAGER:
                request = 0;
                break;
            case KEY_DAY_TIME_THEME:
                request = 1;
                break;
            case KEY_NIGHT_TIME_THEME:
                request = 2;
                break;
        }

        startActivity(new Intent(getActivity(), ThemesActivity.class).putExtra("request", request));
    }

    private void setMorningTime() {
        Calendar currentCalendar = Calendar.getInstance();

        Calendar morningCalendar = (Calendar) currentCalendar.clone();
        morningCalendar.set(Calendar.HOUR, 8);

        setTime(morningCalendar.getTimeInMillis());
    }

    private void setEveningTime() {
        Calendar currentCalendar = Calendar.getInstance();

        Calendar morningCalendar = (Calendar) currentCalendar.clone();
        morningCalendar.set(Calendar.HOUR, 19);

        setTime(morningCalendar.getTimeInMillis());
    }

    private void setTime(long millis) {
        if (getContext() == null) return;

        AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        if (manager != null) {
            manager.setTime(millis);
        }
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
