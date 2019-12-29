package ru.melod1n.schedule.common;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import ru.melod1n.schedule.io.FileStreams;
import ru.melod1n.schedule.util.Util;

public class ExceptionHandler {

    private static final String TAG = "ExceptionHandler";

    private static final Thread.UncaughtExceptionHandler sOldHandler = Thread.getDefaultUncaughtExceptionHandler();
    private static final Thread.UncaughtExceptionHandler EXCEPTION_HANDLER = (thread, ex) -> {
        report(ex);
        if (sOldHandler != null) {
            sOldHandler.uncaughtException(thread, ex);
        }
    };

    private ExceptionHandler() {
    }

    private static void report(Throwable ex) {
        String trace = "Schedule \nVersion: " + AppGlobal.app_version_name + "\nBuild: " + AppGlobal.app_version_code + "\n\n";

        trace +=
                "Android SDK: " + Build.VERSION.SDK + "\n" +
                        "Device: " + Build.DEVICE + "\n" +
                        "Model: " + Build.MODEL + "\n" +
                        "Brand: " + Build.BRAND + "\n" +
                        "Manufacturer: " + Build.MANUFACTURER + "\n" +
                        "Display: " + Build.DISPLAY + "\n";

        trace += "\nLog below: \n\n";
        trace += Log.getStackTraceString(ex);
        Util.copyText(trace);

        String path = Environment.getExternalStorageDirectory() + "/Schedule/crash_logs";

        File dirFile = new File(path);
        if (!dirFile.exists()) dirFile.mkdirs();

        String fileName = "log_" + System.currentTimeMillis() + ".txt";
        createFile(new File(path), fileName, trace);

        File pathFile = new File(AppGlobal.filesDir + "/crash_logs/");
        if (!pathFile.exists()) pathFile.mkdirs();

        createFile(pathFile, fileName, trace);

        AppGlobal.preferences.edit().putBoolean("isCrashed", true).putString("crashLog", trace).apply();
    }

    private static void createFile(File path, String name, String trace) {
        File file = new File(path, name);
        try {
            FileStreams.write(trace, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler(EXCEPTION_HANDLER);
    }
}

