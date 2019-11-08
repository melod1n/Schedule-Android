package ru.melod1n.schedule.common;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.helper.TimeHelper;
import ru.melod1n.schedule.io.FileStreams;
import ru.melod1n.schedule.items.LessonItem;
import ru.melod1n.schedule.items.NoteItem;
import ru.melod1n.schedule.util.ArrayUtil;
import ru.melod1n.schedule.util.Util;

public class Engine {



    public static void checkDatabaseUpdates() {
        if (AppGlobal.preferences.getBoolean("is_db_updated", false)) {
            try {
                File path = new File(AppGlobal.context.getFilesDir() + "/data");

                String s = FileStreams.read(new File(path + "/data.json"));
                JSONObject o = new JSONObject(s);

                JSONArray subs = o.optJSONArray("subjects");
                JSONArray bls = o.optJSONArray("bells");
                JSONArray nts = o.optJSONArray("notes");

                ArrayList<LessonItem> subjects = new ArrayList<>(subs.length());
                //ArrayList<BellItem> bells = new ArrayList<>(subs.length());
                ArrayList<NoteItem> notes = new ArrayList<>(subs.length());

                for (int i = 0; i < subs.length(); i++) {
                    subjects.add(new LessonItem(subs.optJSONObject(i)));
                }

//                for (int i = 0; i < bls.length(); i++) {
//                    bells.add(new BellItem(bls.optJSONObject(i)));
//                }

                for (int i = 0; i < nts.length(); i++) {
                    notes.add(new NoteItem(nts.optJSONObject(i)));
                }

//                if (!ArrayUtil.isEmpty(bells))
//                    CacheStorage.insert(DatabaseHelper.TABLE_BELLS, bells);
                if (!ArrayUtil.isEmpty(notes))
                    CacheStorage.insert(DatabaseHelper.TABLE_NOTES, notes);
                if (!ArrayUtil.isEmpty(subjects))
                    CacheStorage.insert(DatabaseHelper.TABLE_LESSONS, subjects);

                TimeHelper.load();

                AppGlobal.preferences.edit().putBoolean("is_db_updated", false).apply();
            } catch (Exception e) {
                Log.e("parsing data", Log.getStackTraceString(e));
                AppGlobal.preferences.edit().putBoolean("is_db_updated", true).apply();
            }
        }
    }

//    public static int getStartTimeAt(int order) {
//        return bells.get(order).getStart();
//    }
//
//    public static int getEndTimeAt(int order) {
//        return bells.get(order).getEnd();
//    }

    @NonNull
    public static String getTimeByInt(int time) {
        int hours = (int) Math.floor(time / 60);
        int minutes = time - hours * 60;

        return String.format("%s:%s", Util.leadingZero(hours), Util.leadingZero(minutes));
    }
}
