package ru.stwtforever.schedule.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.common.AppGlobal;
import ru.stwtforever.schedule.io.FileStreams;

public class Util {

    public static boolean isFirstLaunch() {
        return AppGlobal.preferences.getBoolean("first_launch", true);
    }

    public static void setFirstLaunch(boolean first) {
        AppGlobal.preferences.edit().putBoolean("first_launch", first).apply();
    }

    public static void restart(Activity activity, Intent extras, boolean anim) {
        Intent intent = new Intent(activity, activity.getClass());
        if (extras != null)
            intent.putExtras(extras);

        activity.startActivity(intent);
        activity.finish();

        if (anim)
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public static void restart(Activity activity, boolean anim) {
        restart(activity, null, anim);
    }

    public static void createFile(File path, String name, String trace) {
        File file = new File(path, name);
        try {
            FileStreams.write(trace, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyText(String text) {
        ClipboardManager cm = (ClipboardManager) AppGlobal.context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, text));
    }

    public static String leadingZero(int num) {
        return num > 9 ? String.valueOf(num) : "0" + num;
    }

    public static boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) AppGlobal.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isAvailable() &&
                cm.getActiveNetworkInfo().isConnected());
    }

    public static String getStringDay(int day) {
        String[] days = AppGlobal.context.getResources().getStringArray(R.array.days);
        return days[day];
    }

    public static int getNumOfCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
            case Calendar.MONDAY:
                day = 0;
                break;
            case Calendar.TUESDAY:
                day = 1;
                break;
            case Calendar.WEDNESDAY:
                day = 2;
                break;
            case Calendar.THURSDAY:
                day = 3;
                break;
            case Calendar.FRIDAY:
                day = 4;
                break;
            case Calendar.SATURDAY:
                day = 5;
                break;
        }

        return day;
    }
}
