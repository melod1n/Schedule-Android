package ru.melod1n.schedule.common;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;

import androidx.preference.PreferenceManager;
import androidx.room.Room;

import java.io.File;

import ru.melod1n.schedule.database.AppDatabase;
import ru.melod1n.schedule.helper.TimeHelper;
import ru.melod1n.schedule.olddatabase.DatabaseHelper;

public class AppGlobal extends Application {

    public static volatile String app_version_name;
    public static volatile int app_version_code;

    public static volatile SQLiteDatabase oldDatabase;
    public static volatile SharedPreferences preferences;

    public static volatile File filesDir;

    public static volatile Resources resources;

    public static volatile InputMethodManager inputMethodManager;
    public static volatile ClipboardManager clipboardManager;

    public static volatile AppDatabase database;

    public static volatile Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        resources = getResources();

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        filesDir = getFilesDir();

        handler = new Handler(getMainLooper());

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            app_version_name = pInfo.versionName;
            app_version_code = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        oldDatabase = DatabaseHelper.get(this).getWritableDatabase();

        database =
                Room.databaseBuilder(this, AppDatabase.class, "database")
                        .fallbackToDestructiveMigration()
                        .build();

        TimeManager.init();
        ExceptionHandler.init();
        TimeHelper.init();
        ThemeEngine.init();
    }
}
