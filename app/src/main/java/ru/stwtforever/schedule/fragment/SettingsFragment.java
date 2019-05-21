package ru.stwtforever.schedule.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.greenrobot.eventbus.EventBus;

import ru.stwtforever.schedule.AboutActivity;
import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.common.AppGlobal;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.db.CacheStorage;
import ru.stwtforever.schedule.db.DatabaseHelper;
import ru.stwtforever.schedule.helper.TimeHelper;
import ru.stwtforever.schedule.util.Util;
import ru.stwtforever.schedule.view.TimePickerDialog;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String KEY_ABOUT = "about";
    private static final String KEY_LESSONS_START = "lessons_start";
    private static final String KEY_LESSON_BREAK_LENGTH = "lesson_break_length";
    public static final String KEY_DARK_THEME = "dark_theme";
    private static final String KEY_BELLS_COUNT = "bells_count";
    static final String KEY_EXPAND_CURRENT_DAY = "expand_current_day";
    static final String KEY_SELECT_CURRENT_DAY = "select_current_day";
    public static final String KEY_SHOW_ERROR = "show_error";
    public static final String KEY_OPEN_ON_START = "open_on_start";

    private int bells, lesson, break_;

    private Toolbar toolbar;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
    }

    @Override
    public void onCreatePreferences(Bundle p1, String p2) {
        setPreferencesFromResource(R.xml.prefs, p2);

        findPreference(KEY_DARK_THEME).setOnPreferenceClickListener(this);
        findPreference(KEY_LESSONS_START).setOnPreferenceClickListener(this);
        findPreference(KEY_LESSON_BREAK_LENGTH).setOnPreferenceClickListener(this);
        findPreference(KEY_BELLS_COUNT).setOnPreferenceClickListener(this);
        findPreference(KEY_ABOUT).setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_ABOUT:
                startAboutActivity();
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
            case KEY_DARK_THEME:
                switchTheme(!ThemeManager.isDark());
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {

        }
        return true;
    }

    private void switchTheme(boolean newValue) {
        ThemeManager.switchTheme(newValue);

        Util.restart(getActivity(), new Intent().putExtra("from_settings", true), true);
    }

    private void showStartLessonsDialog() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), true);

        int hours = TimeHelper.getHours();
        int minutes = TimeHelper.getMinutes();

        dialog.setHintTime(hours, minutes);
        dialog.setTime(hours, minutes);

        dialog.setOnChoosedTimeListener(new TimePickerDialog.OnChoosedTimeListener() {

            @Override
            public void onChoosedTime(int hours, int minutes) {
                setLessonsStart(hours, minutes);
            }
        });
        dialog.show();
    }

    private void showNumBell() {
        View v = getLayoutInflater().inflate(R.layout.choose_bells_count, null, false);

        NumberPicker picker = v.findViewById(R.id.picker);
        picker.setMaxValue(10);
        picker.setMinValue(1);

        picker.setValue(AppGlobal.preferences.getInt("bells_count", 6));

        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int old_val, int new_val) {
                bells = new_val;
            }
        });

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.bells_count)
                .setView(v).setNegativeButton(android.R.string.cancel, null).setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                AppGlobal.preferences.edit().putInt("bells_count", bells).apply();
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
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.warning);
        adb.setMessage(getString(R.string.recreate_bells) + "?");
        adb.setNegativeButton(R.string.no, null);
        adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                CacheStorage.delete(DatabaseHelper.TABLE_BELLS);
                TimeHelper.load();
                EventBus.getDefault().post(new Object[]{"bells_update"});
            }
        });
        adb.create().show();
    }

    private void showLessonBreakLength() {
        TimePickerDialog subject = new TimePickerDialog(getContext());

        subject.setHintNum(TimeHelper.lesson_length);
        subject.setNum(TimeHelper.lesson_length);

        subject.setOnChoosedNumListener(new TimePickerDialog.OnChoosedNumListener() {

            @Override
            public void onChoosedNum(int num) {
                lesson = num;

                TimePickerDialog break_ = new TimePickerDialog(getContext());

                break_.setHintNum(TimeHelper.break_length);
                break_.setNum(TimeHelper.break_length);

                break_.setOnChoosedNumListener(new TimePickerDialog.OnChoosedNumListener() {

                    @Override
                    public void onChoosedNum(int num) {
                        SettingsFragment.this.break_ = num;
                        TimeHelper.lesson_length = lesson;
                        TimeHelper.break_length = SettingsFragment.this.break_;
                        TimeHelper.save();
                        showConfirmRecreateDialog();
                    }
                });

                break_.show();
            }
        });

        subject.show();
    }

    private void startAboutActivity() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }
}
