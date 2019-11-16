package ru.melod1n.schedule.database;


import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ru.melod1n.schedule.items.DayItem;
import ru.melod1n.schedule.items.LessonItem;
import ru.melod1n.schedule.items.LocationItem;
import ru.melod1n.schedule.items.NoteItem;
import ru.melod1n.schedule.items.ParticipantItem;
import ru.melod1n.schedule.items.SubjectItem;
import ru.melod1n.schedule.items.TeacherItem;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Util;

import static ru.melod1n.schedule.common.AppGlobal.database;
import static ru.melod1n.schedule.database.DatabaseHelper.CLASSROOM;
import static ru.melod1n.schedule.database.DatabaseHelper.DAY_OF_WEEK;
import static ru.melod1n.schedule.database.DatabaseHelper.DAY_OF_YEAR;
import static ru.melod1n.schedule.database.DatabaseHelper.ID;
import static ru.melod1n.schedule.database.DatabaseHelper.LESSONS;
import static ru.melod1n.schedule.database.DatabaseHelper.LESSON_TYPE;
import static ru.melod1n.schedule.database.DatabaseHelper.LESSON_TYPE_CUSTOM;
import static ru.melod1n.schedule.database.DatabaseHelper.ORDER;
import static ru.melod1n.schedule.database.DatabaseHelper.PARTICIPANTS;
import static ru.melod1n.schedule.database.DatabaseHelper.POSITION;
import static ru.melod1n.schedule.database.DatabaseHelper.START_TIME;
import static ru.melod1n.schedule.database.DatabaseHelper.SUBJECT;
import static ru.melod1n.schedule.database.DatabaseHelper.TABLE_DAYS;
import static ru.melod1n.schedule.database.DatabaseHelper.TABLE_LESSONS;
import static ru.melod1n.schedule.database.DatabaseHelper.TABLE_NOTES;
import static ru.melod1n.schedule.database.DatabaseHelper.TABLE_THEMES;
import static ru.melod1n.schedule.database.DatabaseHelper.TEACHER;
import static ru.melod1n.schedule.database.DatabaseHelper.TEXT;
import static ru.melod1n.schedule.database.DatabaseHelper.THEME_ID;
import static ru.melod1n.schedule.database.DatabaseHelper.THEME_OBJECT;
import static ru.melod1n.schedule.database.DatabaseHelper.TITLE;

public class CacheStorage {

    private static Cursor selectCursor(String table, @NonNull String column, Object value) {
        return QueryBuilder.query()
                .select("*").from(table)
                .where(column.concat(" = ").concat(String.valueOf(value)))
                .asCursor(database);
    }

    private static Cursor selectCursor(String table, String column, @NonNull int... ids) {
        StringBuilder where = new StringBuilder(5 * ids.length);

        where.append(column);
        where.append(" = ");
        where.append(ids[0]);
        for (int i = 1; i < ids.length; i++) {
            where.append(" OR ");
            where.append(column);
            where.append(" = ");
            where.append(ids[i]);
        }
        return selectCursor(table, where.toString());
    }

    private static Cursor selectCursor(String table, String where) {
        return QueryBuilder.query()
                .select("*").from(table).where(where)
                .asCursor(database);
    }

    private static Cursor selectCursor(String table) {
        return QueryBuilder.query()
                .select("*").from(table)
                .asCursor(database);
    }

    private static int getInt(@NonNull Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private static String getString(@NonNull Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static byte[] getBlob(@NonNull Cursor cursor, String columnName) {
        return cursor.getBlob(cursor.getColumnIndex(columnName));
    }

    public static ArrayList<ThemeItem> getThemes() {
        return getThemes(null);
    }

    public static ArrayList<ThemeItem> getThemes(String id) {
        Cursor cursor = id == null ? selectCursor(TABLE_THEMES) : selectCursor(TABLE_THEMES, THEME_ID, id);

        ArrayList<ThemeItem> items = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext()) {
            items.add(parseTheme(cursor));
        }

        return items;
    }

//    public static ArrayList<LessonItem> getSubjects(int day) {
//        Cursor c = selectCursor(TABLE_LESSONS, DAY, day);
//
//        ArrayList<LessonItem> subs = new ArrayList<>(c.getCount());
//        ArrayList<BellItem> bells = getBells(day);
//
//        int position = 0;
//
//        while (c.moveToNext()) {
//            LessonItem item = parseLesson(c);
//
//            if (!ArrayUtil.isEmpty(bells) && position <= bells.size() - 1) {
//                BellItem bell = bells.get(position);
////                item.setStart(bell.getStart());
////                item.setEnd(bell.getEnd());
//            }
//
//            subs.add(item);
//            position++;
//        }
//
//        c.close();
//        return subs;
//    }

    public static ArrayList<DayItem> getDays() {
        return getDays(-1);
    }

    public static ArrayList<DayItem> getDays(int order) {
        Cursor cursor = order == -1 ? selectCursor(TABLE_DAYS) : selectCursor(TABLE_DAYS, POSITION, order);

        ArrayList<DayItem> items = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            items.add(parseDay(cursor));
        }

        cursor.close();
        return items;
    }

    public static ArrayList<LessonItem> getSubjects() {
        Cursor c = selectCursor(TABLE_LESSONS);

        ArrayList<LessonItem> subs = new ArrayList<>(c.getCount());
        while (c.moveToNext()) {
            subs.add(parseLesson(c));
        }

        c.close();
        return subs;
    }

    public static ArrayList<NoteItem> getNotes() {
        Cursor c = selectCursor(TABLE_NOTES);

        ArrayList<NoteItem> notes = new ArrayList<>(c.getCount());
        while (c.moveToNext()) {
            notes.add(parseNote(c));
        }

        c.close();
        return notes;
    }

//    public static ArrayList<BellItem> getBells() {
//        Cursor c = selectCursor(TABLE_BELLS);
//
//        ArrayList<BellItem> items = new ArrayList<>(c.getCount());
//        while (c.moveToNext()) {
//            items.add(parseBell(c));
//        }
//
//        c.close();
//        return items;
//    }
//
//    public static ArrayList<BellItem> getBells(int day) {
//        Cursor c = selectCursor(TABLE_BELLS, DAY, day);
//
//        ArrayList<BellItem> items = new ArrayList<>(c.getCount());
//        while (c.moveToNext()) {
//            items.add(parseBell(c));
//        }
//
//        c.close();
//        return items;
//    }

    public static void insert(String table, Object item) {
        ArrayList<Object> array = new ArrayList<>();
        array.add(item);

        insert(table, array);
    }

    public static void update(String table, Object item, String where, Object args) {
        ArrayList<Object> array = new ArrayList<>();
        array.add(item);

        update(table, array, where, args);
    }

    public static void insert(String table, @NonNull ArrayList values) {
        database.beginTransaction();

        ContentValues cv = new ContentValues();
        for (Object item : values) {
            switch (table) {
                case TABLE_NOTES:
                    putValues(cv, (NoteItem) item);
                    break;
                case TABLE_LESSONS:
                    putValues(cv, (LessonItem) item);
                    break;
//                case TABLE_BELLS:
//                    putValues(cv, (BellItem) item);
//                    break;
                case TABLE_THEMES:
                    putValues(cv, (ThemeItem) item);
                    break;
            }

            try {
                database.insert(table, null, cv);
            } catch (Exception ignored) {
            }

            cv.clear();
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public static void update(String table, @NonNull ArrayList values, String where, Object args) {
        database.beginTransaction();

        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.size(); i++) {
            Object item = values.get(i);
            switch (table) {
                case TABLE_NOTES:
                    putValues(cv, (NoteItem) item);
                    break;
                case TABLE_LESSONS:
                    putValues(cv, (LessonItem) item);
                    break;
//                case TABLE_BELLS:
//                    putValues(cv, (BellItem) item);
//                    break;
                case TABLE_THEMES:
                    putValues(cv, (ThemeItem) item);
                    break;
            }

            try {
                database.update(table, cv, where, new String[]{String.valueOf(args)});
            } catch (Exception ignored) {
            }
            cv.clear();
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public static void delete(String table, String where) {
        database.delete(table, where, null);
    }

    public static void delete(String table) {
        database.delete(table, null, null);
    }

    private static DayItem parseDay(Cursor cursor) {
        DayItem item = new DayItem();

        item.setDate(getInt(cursor, START_TIME));
        item.setDayOfWeek(getInt(cursor, DAY_OF_WEEK));
        item.setDayOfYear(getInt(cursor, DAY_OF_YEAR));

        ArrayList<LessonItem> lessons = (ArrayList<LessonItem>) Util.deserialize(getBlob(cursor, LESSONS));
        item.setLessons(lessons == null ? new ArrayList<>() : lessons);

        return item;
    }

    private static void putValues(@NonNull ContentValues values, @NonNull DayItem item) {
        values.put(START_TIME, item.getDate());
        values.put(DAY_OF_WEEK, item.getDayOfWeek());
        values.put(DAY_OF_YEAR, item.getDayOfYear());
        values.put(LESSONS, Util.serialize(item.getLessons()));
    }

    private static void putValues(@NonNull ContentValues values, @NonNull ThemeItem item) {
        values.put(THEME_ID, item.getId());
        values.put(THEME_OBJECT, Util.serialize(item));
    }

    @Nullable
    private static ThemeItem parseTheme(Cursor cursor) {
        ThemeItem item = (ThemeItem) Util.deserialize(getBlob(cursor, THEME_OBJECT));

        if (item == null) return null;

        item.setId(getString(cursor, THEME_ID));

        return item;
    }

    private static LessonItem parseLesson(Cursor cursor) {
        LessonItem item = new LessonItem();

        item.setOrder(getInt(cursor, ORDER));
        item.setLessonType(LessonItem.getLessonType(getInt(cursor, LESSON_TYPE)));
        item.setLessonTypeCustom(getString(cursor, LESSON_TYPE_CUSTOM));
        item.setSubject((SubjectItem) Util.deserialize(getBlob(cursor, SUBJECT)));
        item.setTeacher((TeacherItem) Util.deserialize(getBlob(cursor, TEACHER)));
        item.setClassRoom((LocationItem) Util.deserialize(getBlob(cursor, CLASSROOM)));

        ArrayList<ParticipantItem> participants = (ArrayList<ParticipantItem>) Util.deserialize(getBlob(cursor, PARTICIPANTS));
        item.setParticipants(participants == null ? new ArrayList<>() : participants);

        return item;
    }

    private static void putValues(@NonNull ContentValues values, @NonNull LessonItem item) {
        values.put(ORDER, item.getOrder());
        values.put(LESSON_TYPE, LessonItem.getLessonType(item.getLessonType()));
        values.put(LESSON_TYPE_CUSTOM, item.getLessonTypeCustom());
        values.put(SUBJECT, Util.serialize(item.getSubject()));
        values.put(TEACHER, Util.serialize(item.getTeacher()));
        values.put(CLASSROOM, Util.serialize(item.getClassRoom()));
        values.put(PARTICIPANTS, Util.serialize(item.getParticipants()));
    }

    private static NoteItem parseNote(Cursor c) {
        NoteItem item = new NoteItem();

        item.id = getInt(c, ID);
        item.position = getInt(c, POSITION);
        item.title = getString(c, TITLE);
        item.text = getString(c, TEXT);

        return item;
    }

//    private static BellItem parseBell(Cursor c) {
//        BellItem i = new BellItem();
//
//        i.id = getInt(c, ID);
//        i.start = getInt(c, START);
//        i.end = getInt(c, END);
//        i.day = getInt(c, DAY);
//
//        return i;
//    }

    private static void putValues(@NonNull ContentValues cv, @NonNull NoteItem item) {
        cv.put(TITLE, item.title);
        cv.put(POSITION, item.position);
        cv.put(TEXT, item.text);
    }

//    private static void putValues(@NonNull ContentValues cv, @NonNull BellItem item) {
//        cv.put(START, item.start);
//        cv.put(END, item.end);
//        cv.put(DAY, item.day);
//    }
}
