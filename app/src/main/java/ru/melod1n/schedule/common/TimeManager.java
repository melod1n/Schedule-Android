package ru.melod1n.schedule.common;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.melod1n.schedule.util.Util;

public class TimeManager {

    private static int currentHour;

    private static ArrayList<OnHourChangeListener> onHourChangeListeners = new ArrayList<>();

    public static void init() {
        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR);

        processThread.start();
    }

    private static Timer timer = new Timer();

    private static Thread processThread = new Thread(() -> timer.schedule(new TimerTask() {
        @Override
        public void run() {
            Date date = new Date(System.currentTimeMillis());

            String s = new SimpleDateFormat("HH").format(date);

            if (onHourChangeListeners != null) {
                for (OnHourChangeListener onHourChangeListener : onHourChangeListeners)
                    onHourChangeListener.onHourChange(Util.tryToParseInt(s));
            }
        }
    }, 0, DateUtils.SECOND_IN_MILLIS));

    public static int getCurrentHour() {
        return currentHour;
    }

    public static void setOnHourChangeListener(OnHourChangeListener onHourChangeListeners) {
        TimeManager.onHourChangeListeners.add(onHourChangeListeners);
    }

    public static void removeOnHourChangeListener(OnHourChangeListener onHourChangeListener) {
        TimeManager.onHourChangeListeners.remove(onHourChangeListener);
    }

    public interface OnHourChangeListener {
        void onHourChange(int currentHour);
    }
}
