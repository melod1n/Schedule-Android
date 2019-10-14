package ru.melod1n.schedule.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import ru.melod1n.schedule.common.AppGlobal;

public class DatabaseHelper extends SQLiteOpenHelper {

    //COLUMNS
    public static final String TABLE_THEMES = "themes";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_LESSONS = "lessons";
    public static final String TABLE_BELLS = "bells";
    public static final String TABLE_DAYS = "days";
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String TABLE_TEACHERS = "teachers";
    public static final String TABLE_CLASSROOMS = "classrooms";
    public static final String TABLE_PARTICIPANTS = "participants";

    //DB FIELDS
    public static final String ID = "id";
    public static final String POSITION = "position";
    public static final String TITLE = "title";

    public static final String NAME = "name";
    public static final String CAB = "cab";
    public static final String HOMEWORK = "homework";
    public static final String DAY = "day";
    public static final String START = "start";
    public static final String END = "end_";
    public static final String TEXT = "text";


    public static final String START_TIME = "start_time";
    public static final String DAY_OF_WEEK = "day_of_week";
    public static final String DAY_OF_YEAR = "day_of_year";
    public static final String LESSONS = "lessons";

    public static final String ORDER = "order_";
    public static final String LESSON_TYPE = "lesson_type";
    public static final String LESSON_TYPE_CUSTOM = "lesson_type_custom";
    public static final String SUBJECT = "subject";
    public static final String TEACHER = "teacher";
    public static final String CLASSROOM = "classroom";
    public static final String PARTICIPANTS = "participants";

    public static final String BUILDING = "building";

    public static final String SUBGROUP = "subgroup";

    public static final String COLOR_POSITION = "color_position";

    public static final String THEME_OBJECT = "theme";
    public static final String THEME_ID = "theme_id";

    //TABLES

    private final static String SQL_CREATE_TABLE_DAYS = "create table " + TABLE_DAYS +
            " (" + POSITION + " integer primary key unique on conflict replace, " +
            START_TIME + " integer, " +
            DAY_OF_WEEK + " integer, " +
            DAY_OF_YEAR + " integer, " +
            LESSONS + " blob" +
            ");";

    private final static String SQL_CREATE_TABLE_LESSONS = "create table " + TABLE_LESSONS +
            " (" + ORDER + " integer, " +
            LESSON_TYPE + " text, " +
            LESSON_TYPE_CUSTOM + " text, " +
            SUBJECT + " blob, " +
            TEACHER + " blob, " +
            CLASSROOM + " blob, " +
            PARTICIPANTS + " blob" +
            ");";

    private final static String SQL_CREATE_TABLE_SUBJECTS = "create table " + TABLE_SUBJECTS +
            " (" + ID + " integer primary key unique on conflict replace, " +
            TITLE + " text, " +
            COLOR_POSITION + " integer" +
            ");";

    private final static String SQL_CREATE_TABLE_TEACHERS = "create table " + TABLE_TEACHERS +
            " (" + ID + " integer primary key unique on conflict replace, " +
            TITLE + " text" +
            ");";

    private final static String SQL_CREATE_TABLE_CLASSROOMS = "create table " + TABLE_CLASSROOMS +
            " (" + ID + " integer primary key unique on conflict replace, " +
            TITLE + " text, " +
            BUILDING + " text" +
            ");";

    private final static String SQL_CREATE_TABLE_PARTICIPANTS = "create table " + TABLE_PARTICIPANTS +
            " (" + ID + " integer primary key unique on conflict replace, " +
            TITLE + " text, " +
            SUBGROUP + " text" +
            ");";


    private final static String SQL_CREATE_TABLE_BELLS = "create table " + TABLE_BELLS +
            " (" + ID + " integer primary key autoincrement, " +
            DAY + " integer, " +
            START + " integer, " +
            END + " integer" +
            ");";

    private final static String SQL_CREATE_TABLE_NOTES = "create table " + TABLE_NOTES +
            " (" + ID + " integer primary key autoincrement, " +
            TITLE + " text, " +
            TEXT + " text, " +
            POSITION + " integer" +
            ");";

    private final static String SQL_CREATE_TABLE_THEMES = "create table " + TABLE_THEMES +
            " (" + ID + " integer primary key autoincrement, " +
            THEME_ID + " text unique on conflict replace, " +
            THEME_OBJECT + " blob" +
            ");";

    //DROP TABLES
    private static final String SQL_DELETE_LESSONS = "drop table if exists " + TABLE_LESSONS;
    private static final String SQL_DELETE_NOTES = "drop table if exists " + TABLE_NOTES;
    private static final String SQL_DELETE_BELLS = "drop table if exists " + TABLE_BELLS;
    private static final String SQL_DELETE_DAYS = "drop table if exists " + TABLE_DAYS;
    private static final String SQL_DELETE_SUBJECTS = "drop table if exists " + TABLE_SUBJECTS;
    private static final String SQL_DELETE_TEACHERS = "drop table if exists " + TABLE_TEACHERS;
    private static final String SQL_DELETE_CLASSROOMS = "drop table if exists " + TABLE_CLASSROOMS;
    private static final String SQL_DELETE_PARTICIPANTS = "drop table if exists " + TABLE_PARTICIPANTS;
    private static final String SQL_DELETE_THEMES = "drop table if exists " + TABLE_THEMES;

    //DB INFO
    private static final String DB_NAME = "schedule.db";
    private static final int DB_VERSION = 32;

    private static DatabaseHelper instance;

    public DatabaseHelper() {
        super(AppGlobal.context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper get() {
        if (instance == null)
            instance = new DatabaseHelper();

        return instance;
    }

    private static void dropTables(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_THEMES);
        db.execSQL(SQL_DELETE_DAYS);
        db.execSQL(SQL_DELETE_LESSONS);
        db.execSQL(SQL_DELETE_SUBJECTS);
        db.execSQL(SQL_DELETE_TEACHERS);
        db.execSQL(SQL_DELETE_CLASSROOMS);
        db.execSQL(SQL_DELETE_PARTICIPANTS);

        db.execSQL(SQL_DELETE_NOTES);
        db.execSQL(SQL_DELETE_BELLS);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_THEMES);
        db.execSQL(SQL_CREATE_TABLE_DAYS);
        db.execSQL(SQL_CREATE_TABLE_LESSONS);
        db.execSQL(SQL_CREATE_TABLE_SUBJECTS);
        db.execSQL(SQL_CREATE_TABLE_TEACHERS);
        db.execSQL(SQL_CREATE_TABLE_CLASSROOMS);
        db.execSQL(SQL_CREATE_TABLE_PARTICIPANTS);

        db.execSQL(SQL_CREATE_TABLE_NOTES);
        db.execSQL(SQL_CREATE_TABLE_BELLS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int new_version) {
        // AppGlobal.saveData();

        dropTables(db);
        onCreate(db);

        //TODO: доделать
        //AppGlobal.preferences.edit().putBoolean("is_db_updated", true).apply();
    }
}
