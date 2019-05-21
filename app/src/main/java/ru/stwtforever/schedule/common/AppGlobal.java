package ru.stwtforever.schedule.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.io.File;
import java.util.ArrayList;

import ru.stwtforever.schedule.adapter.items.BellItem;
import ru.stwtforever.schedule.adapter.items.NoteItem;
import ru.stwtforever.schedule.adapter.items.SubjectItem;
import ru.stwtforever.schedule.db.CacheStorage;
import ru.stwtforever.schedule.db.DatabaseHelper;
import ru.stwtforever.schedule.helper.TimeHelper;
import ru.stwtforever.schedule.util.ArrayUtil;
import ru.stwtforever.schedule.util.Util;

public class AppGlobal extends Application {

    public static volatile Context context;

    public static volatile String app_version_name;
    public static volatile int app_version_code;

    public static volatile SQLiteDatabase database;
    public static volatile SharedPreferences preferences;

    public static void saveData() {
        if (database == null) return;
        if (!database.isOpen() || database.isReadOnly()) return;
        ArrayList<SubjectItem> subjects = CacheStorage.getSubjects();
        ArrayList<NoteItem> notes = CacheStorage.getNotes();
        ArrayList<BellItem> bells = CacheStorage.getBells();

        String subs = "";

        if (!ArrayUtil.isEmpty(subjects)) {
            for (int i = 0; i < subjects.size(); i++) {
                SubjectItem item = subjects.get(i);
                String s = "{\n" +
                        "     \"id\": " + item.getId() + ",\n" +
                        "     \"color\": " + item.getColor() + ",\n" +
                        "     \"name\": \"" + item.getName() + "\",\n" +
                        "     \"cab\": \"" + item.getCab() + "\",\n" +
                        "     \"homework\": \"" + item.getHomework() + "\",\n" +
                        "     \"day\": " + item.getDay() +
                        "\n}" + (i == subjects.size() - 1 ? "" : ", ");

                subs += s;
            }
        }

        String nts = "";

        if (!ArrayUtil.isEmpty(notes)) {
            for (int i = 0; i < notes.size(); i++) {
                NoteItem item = notes.get(i);
                String s = "{\n" +
                        "     \"id\": " + item.getId() + ",\n" +
                        "     \"title\": \"" + item.getTitle() + "\",\n" +
                        "     \"text\": \"" + item.getText() + "\",\n" +
                        "     \"color\": " + item.getColor() +
                        "\n}" + (i == notes.size() - 1 ? "" : ", ");

                nts += s;
            }
        }

        String bls = "";

        if (!ArrayUtil.isEmpty(bells)) {
            for (int i = 0; i < bells.size(); i++) {
                BellItem item = bells.get(i);
                String s = "{\n" +
                        "     \"id\": " + item.getId() + ",\n" +
                        "     \"start\": " + item.getStart() + ",\n" +
                        "     \"end\": " + item.getEnd() + ",\n" +
                        "     \"day\": " + item.day +
                        "\n}" + (i == bells.size() - 1 ? "" : ", ");

                bls += s;
            }
        }

        String json = "{\n" +
                "\"subjects\": [" + subs + "],\n" +
                "\"notes\": [" + nts + "],\n" +
                "\"bells\": [" + bls + "]" +
                "\n}";


        File f = context.getFilesDir();
        File path = new File(f + "/data/");

        if (!path.exists()) path.mkdirs();

        Util.createFile(path, "data.json", json);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            app_version_name = pInfo.versionName;
            app_version_code = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        database = DatabaseHelper.get().getWritableDatabase();

        CrashManager.init();
        TimeHelper.init();
        ThemeManager.init();
        Engine.checkDatabaseUpdates();

        AppCenter.start(this, "70c5472c-d7b2-4977-bdba-565adc092566", Analytics.class, Crashes.class);
    }
}
