package ru.melod1n.schedule.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.io.BytesOutputStream;
import ru.melod1n.schedule.io.FileStreams;

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

    @NonNull
    public static String readFileFromAssets(String fileName) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = AppGlobal.context.getResources().getAssets().open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return returnString.toString();
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

    public static byte[] serialize(Object source) {
        try {
            BytesOutputStream bos = new BytesOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);

            out.writeObject(source);
            out.close();

            return bos.getByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object deserialize(byte[] source) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(source);
            ObjectInputStream in = new ObjectInputStream(bis);

            Object o = in.readObject();
            in.close();

            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStringDay(int day) {
        String[] days = AppGlobal.context.getResources().getStringArray(R.array.days);
        return days[day];
    }

    public static String getMonthString(int month) {
        String[] months = AppGlobal.context.getResources().getStringArray(R.array.months);
        return months[month];
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

    public static int px(float dp) {
        return (int) (dp * (AppGlobal.context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dp(float px) {
        return (int) (px / (AppGlobal.context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int tryToParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return 0;
        }
    }
}
