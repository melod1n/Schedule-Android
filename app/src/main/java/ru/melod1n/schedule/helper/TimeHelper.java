package ru.melod1n.schedule.helper;

import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;

import ru.melod1n.schedule.items.BellItem;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;

public class TimeHelper {

    private static final int DAY = 1440;

    public static int lesson_length, break_length, start_value, bells_count;
    public static String start_time;

    public TimeHelper(int l, int b, String start) {
        lesson_length = l;
        break_length = b;
        start_time = start;
        load();
    }

    public static void init() {
        SharedPreferences prefs = AppGlobal.preferences;
        lesson_length = prefs.getInt("lesson", 0);
        break_length = prefs.getInt("break", 0);
        start_time = prefs.getString("start", "");
        bells_count = prefs.getInt("bells_count", 6);
        start_value = getStart(start_time);
    }

    public static void load() {
        load(-1);
    }

    public static void load(int day) {
        start_value = getStart(start_time);

        SharedPreferences prefs = AppGlobal.preferences;
        lesson_length = prefs.getInt("lesson", -1);
        break_length = prefs.getInt("break", -1);
        bells_count = prefs.getInt("bells_count", -1);

        createTimetable(day);
    }

    public static void save() {
        SharedPreferences.Editor ed = AppGlobal.preferences.edit();
        ed.putInt("lesson", lesson_length);
        ed.putInt("break", break_length);
        ed.putString("start", start_time);
        ed.apply();
        init();
    }

    public static String getStart(int time) {
        double hours = Math.floor(time / 60);
        double minutes = Math.floor(time - hours * 60);
        return (int) hours + ":" + (int) minutes;
    }

    public static int getHours(int time) {
        double hours = Math.floor(time / 60);
        return (int) hours;
    }

    public static int getMinutes(int time) {
        return (int) Math.floor(time - getHours(time) * 60);
    }

    public static int getHours() {
        return getHours(start_value);
    }

    public static int getMinutes() {
        return getMinutes(start_value);
    }

    public static int getStart(String start) {
        if (!TextUtils.isEmpty(start)) {
            String[] s = start.split(":");
            int h = Integer.parseInt(s[0]);
            int m = Integer.parseInt(s[1]);

            return h * 60 + m;
        }
        return -1;
    }

    public static void createTimetable(int day) {
        int start_time = start_value;
        int end_time = start_time + break_length;

        ArrayList<BellItem> bells = new ArrayList<>();

        for (int j = 0; j < (day != -1 ? 1 : 6); j++) {

            start_time = start_value;
            end_time = start_time + lesson_length;

            for (int i = 0; i < bells_count; i++) {

                if (DAY - start_time < 0) {
                    while (DAY - start_time < 0)
                        start_time = -1 * (DAY - start_time);
                } else if (DAY - start_time == 0) {
                    start_time = 0;
                }

                if (DAY - end_time < 0) {
                    while (DAY - end_time < 0)
                        end_time = -1 * (DAY - end_time);
                } else if (DAY - end_time == 0) {
                    end_time = 0;
                }

                BellItem item = new BellItem();
                item.setStart(start_time);
                item.setEnd(end_time);
                item.setId(i);
                item.setDay(day != -1 ? day : j);
                bells.add(item);

                start_time = end_time + break_length;
                end_time = start_time + lesson_length;
            }
        }

        CacheStorage.insert(DatabaseHelper.TABLE_BELLS, bells);
    }
}
