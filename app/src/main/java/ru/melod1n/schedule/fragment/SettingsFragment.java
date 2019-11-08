package ru.melod1n.schedule.fragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import org.greenrobot.eventbus.EventBus;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.SettingsActivity;
import ru.melod1n.schedule.ThemesActivity;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.helper.TimeHelper;
import ru.melod1n.schedule.util.Util;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String KEY_LESSONS_START = "lessons_start";
    private static final String KEY_LESSON_BREAK_LENGTH = "lesson_break_length";
    private static final String KEY_SETUP_BELLS = "setup_bells";
    private static final String KEY_SETUP_APP = "setup_app";

    private static final String KEY_APPEARANCE = "appearance";

    static final String KEY_BELLS_COUNT = "bells_count";
    static final String KEY_EXPAND_CURRENT_DAY = "expand_current_day";
    static final String KEY_SELECT_CURRENT_DAY = "select_current_day";

    public static final String KEY_SHOW_ERROR = "show_error";
    public static final String KEY_OPEN_ON_START = "open_on_start";
    public static final String KEY_THEME = "theme";

    private int bells_count, dur_lesson, dur_break;

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

        Preference theme = findPreference(KEY_THEME);
        Preference lessonsStart = findPreference(KEY_LESSONS_START);
        Preference lessonBreakLength = findPreference(KEY_LESSON_BREAK_LENGTH);
        Preference bellsCount = findPreference(KEY_BELLS_COUNT);
        Preference setupBells = findPreference(KEY_SETUP_BELLS);
        Preference setupApp = findPreference(KEY_SETUP_APP);

        PreferenceScreen appearance = findPreference(KEY_APPEARANCE);

        if (appearance != null) {
            appearance.setOnPreferenceClickListener(this);
        }

        PreferenceScreen rootScreen = getPreferenceScreen();

        applyTintInPreferenceScreen(rootScreen);

        if (theme != null) {
            theme.setOnPreferenceClickListener(this);

            theme.setSummary(getString(R.string.theme_summary, ThemeEngine.getCurrentTheme().getTitle(), ThemeEngine.getCurrentTheme().getAuthor()));
            //theme.setEnabled(false);
        }

        if (lessonsStart != null)
            lessonsStart.setOnPreferenceClickListener(this);

        if (lessonBreakLength != null)
            lessonBreakLength.setOnPreferenceClickListener(this);

        if (bellsCount != null)
            bellsCount.setOnPreferenceClickListener(this);

        if (setupBells != null)
            setupBells.setOnPreferenceClickListener(this);

        if (setupApp != null)
            setupApp.setOnPreferenceClickListener(this);

    }

    private void setTitle() {
        int title = -1;
        switch (currentPreferenceLayout) {
            case R.xml.fragment_settings:
                title = R.string.settings;
                break;
            case R.xml.category_appearance:
                title = R.string.pref_appearance_title;
                break;
        }

        getActivity().setTitle(title);
    }

    private void applyTintInPreferenceScreen(PreferenceScreen rootScreen) {
        if (rootScreen.getPreferenceCount() > 0) {
            for (int i = 0; i < rootScreen.getPreferenceCount(); i++) {
                Preference preference = rootScreen.getPreference(i);
                tintPreference(preference);
            }
        }
    }

    private void tintPreference(@NonNull Preference preference) {
        if (preference.getIcon() != null)
            preference.getIcon().setTint(!ThemeEngine.getCurrentTheme().isMd2() ? ThemeEngine.getCurrentTheme().getColorAccent() : ThemeEngine.getColorMain());
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_APPEARANCE:
                currentPreferenceLayout = R.xml.category_appearance;
                init();
                break;
            case KEY_THEME:
                startThemesActivity();
                break;
            case KEY_LESSONS_START:
                showStartLessonsDialog();
                break;
            case KEY_LESSON_BREAK_LENGTH:
                showLessonBreakLength();
                break;
            case KEY_BELLS_COUNT:
                showNumBell();
                break;
            case KEY_SETUP_BELLS:
                Toast.makeText(getActivity(), "In progress...", Toast.LENGTH_SHORT).show();
                break;
            case KEY_SETUP_APP:
                showConfirmSetupAppDialog();
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void showConfirmSetupAppDialog() {
        if (getActivity() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.warning);
        builder.setMessage("Перенастроить приложение?");
        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            Util.setFirstLaunch(true);
            Util.restart(getActivity(), true);
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    private void showStartLessonsDialog() {
        if (getActivity() == null) return;
//        TimePickerDialog dialog = new TimePickerDialog(getActivity(), true);
//
//        dialog.setTitle(R.string.set_lessons_time);
//
//        int hours = TimeHelper.getHours();
//        int minutes = TimeHelper.getMinutes();
//
//        dialog.setHintTime(hours, minutes);
//        dialog.setTime(hours, minutes);
//
//        dialog.setOnChoosedTimeListener(this::setLessonsStart);
//        dialog.show();
    }

    private void showNumBell() {
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.choose_bells_count, null, false);

        NumberPicker picker = v.findViewById(R.id.picker);
        picker.setMaxValue(10);
        picker.setMinValue(1);

        final int bellsCount = AppGlobal.preferences.getInt(KEY_BELLS_COUNT, 0);

        picker.setValue(bellsCount);

        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener((picker1, old_val, new_val) -> bells_count = new_val);

        if (getActivity() == null) return;

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bells_count)
                .setView(v).setNegativeButton(android.R.string.cancel, null).setPositiveButton(R.string.edit, (p1, p2) -> {
            if (bellsCount > bells_count) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.warning);
                builder.setMessage("Количество уроков в каждом дне уменьшится и станет меньше, либо равным количеству звонков. Вы согласны?");
                builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    AppGlobal.preferences.edit().putInt(KEY_BELLS_COUNT, bells_count).apply();
                    showConfirmRecreateDialog();
                });
                builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> showNumBell());
                builder.show();
            } else {
                AppGlobal.preferences.edit().putInt(KEY_BELLS_COUNT, bells_count).apply();
                showConfirmRecreateDialog();
            }

        }).setCancelable(false).create().show();
    }

    private void setLessonsStart(int hours, int minutes) {
        TimeHelper.start_time = hours + ":" + minutes;
        TimeHelper.save();
        showConfirmRecreateDialog();
    }

    private void showConfirmRecreateDialog() {
        if (getActivity() == null) return;
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.warning);
        adb.setMessage(getString(R.string.recreate_bells) + "?");
        adb.setNegativeButton(R.string.no, null);
        adb.setPositiveButton(R.string.yes, (p1, p2) -> {
            CacheStorage.delete(DatabaseHelper.TABLE_BELLS);
            TimeHelper.load();
            updateBellsCount();
        });
        adb.create().show();
    }

    static void updateBellsCount() {
        EventBus.getDefault().post(new Object[]{"bells_update"});
    }

    private void showLessonBreakLength() {
        if (getActivity() == null) return;
//        TimePickerDialog subject = new TimePickerDialog(getActivity());
//
//        subject.setTitle(getString(R.string.lesson_length_title));
//        subject.setHintNum(TimeHelper.lesson_length);
//        subject.setNum(TimeHelper.lesson_length);
//
//        subject.setOnChoosedNumListener(num -> {
//            dur_lesson = num;
//
//            TimePickerDialog break_ = new TimePickerDialog(getActivity());
//
//            break_.setTitle(getString(R.string.break_length));
//            break_.setHintNum(TimeHelper.break_length);
//            break_.setNum(TimeHelper.break_length);
//
//            break_.setOnChoosedNumListener(num1 -> {
//                SettingsFragment.this.dur_break = num1;
//                TimeHelper.lesson_length = dur_lesson;
//                TimeHelper.break_length = SettingsFragment.this.dur_break;
//                TimeHelper.save();
//                showConfirmRecreateDialog();
//            });
//
//            break_.show();
//        });
//
//        subject.show();
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
        ((SettingsActivity) getActivity()).setFragmentElement(currentPreferenceLayout);
        super.onDestroy();
    }
}
