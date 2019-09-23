package ru.melod1n.schedule.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import ru.melod1n.schedule.adapter.items.BellItem;
import ru.melod1n.schedule.adapter.items.NoteItem;
import ru.melod1n.schedule.adapter.items.SubjectItem;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.helper.TimeHelper;
import ru.melod1n.schedule.io.FileStreams;
import ru.melod1n.schedule.util.ArrayUtil;

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

                ArrayList<SubjectItem> subjects = new ArrayList<>(subs.length());
                ArrayList<BellItem> bells = new ArrayList<>(subs.length());
                ArrayList<NoteItem> notes = new ArrayList<>(subs.length());

                for (int i = 0; i < subs.length(); i++) {
                    subjects.add(new SubjectItem(subs.optJSONObject(i)));
                }

                for (int i = 0; i < bls.length(); i++) {
                    bells.add(new BellItem(bls.optJSONObject(i)));
                }

                for (int i = 0; i < nts.length(); i++) {
                    notes.add(new NoteItem(nts.optJSONObject(i)));
                }

                if (!ArrayUtil.isEmpty(bells))
                    CacheStorage.insert(DatabaseHelper.TABLE_BELLS, bells);
                if (!ArrayUtil.isEmpty(notes))
                    CacheStorage.insert(DatabaseHelper.TABLE_NOTES, notes);
                if (!ArrayUtil.isEmpty(subjects))
                    CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, subjects);

                TimeHelper.load();

                AppGlobal.preferences.edit().putBoolean("is_db_updated", false).apply();
            } catch (Exception e) {
                Log.e("parsing data", Log.getStackTraceString(e));
                AppGlobal.preferences.edit().putBoolean("is_db_updated", true).apply();
            }
        }
    }
}
