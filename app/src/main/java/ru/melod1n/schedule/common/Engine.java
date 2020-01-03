package ru.melod1n.schedule.common;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.melod1n.schedule.util.Util;

public class Engine {

    @NonNull
    public static String getTimeByInt(int time) {
        int hours = (int) Math.floor(time / 60);
        int minutes = time - hours * 60;

        return String.format("%s:%s", Util.leadingZero(hours), Util.leadingZero(minutes));
    }

    @NotNull
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());


        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        return day + " " + Util.getMonthString(month);
    }

    public static String getInterim() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        int firstDayOfWeek = Calendar.MONDAY;
        int lastDayOfWeek = Calendar.SATURDAY;
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        final long DAY = DateUtils.DAY_IN_MILLIS;

        long date = calendar.getTimeInMillis();
        long firstDayDate = date - DAY * (currentDay - firstDayOfWeek);
        long lastDayDate = date + DAY * (lastDayOfWeek - currentDay);

        String firstDay = getFormattedDate(firstDayDate);
        String lastDay = getFormattedDate(lastDayDate);

        return firstDay + " — " + lastDay;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDate(long date) {
        //dd.mm format
        return new SimpleDateFormat("dd.MM").format(new Date(date));
    }

    public static void sendEvent(EventInfo info, boolean sticky) {
        if (sticky) {
            EventBus.getDefault().postSticky(info);
        } else {
            EventBus.getDefault().post(info);
        }
    }

    public static void sendEvent(EventInfo info) {
        sendEvent(info, false);
    }

    public static void editPreferences(@NonNull String key, String newValue) {
        AppGlobal.preferences.edit().putString(key, newValue).apply();
    }

    public static String getPrefString(@NonNull String key, @Nullable String defValue) {
        return AppGlobal.preferences.getString(key, defValue == null ? "" : defValue);
    }

    public static String getPrefString(@NonNull String key) {
        return getPrefString(key, null);
    }

    public static boolean getPrefBool(@NonNull String key, boolean defValue) {
        return AppGlobal.preferences.getBoolean(key, defValue);
    }
}
