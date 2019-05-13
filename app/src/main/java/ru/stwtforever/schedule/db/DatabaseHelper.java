package ru.stwtforever.schedule.db;

import android.database.sqlite.*;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	//COLLUMNS
	public static final String TABLE_NOTES = "notes";
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String TABLE_BELLS = "bells";
	
	//DB FIELDS
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String CAB = "cab";
	public static final String HOMEWORK = "homework";
	public static final String DAY = "day";
	public static final String START = "start";
	public static final String END = "end";
	public static final String TITLE = "title";
	public static final String TEXT = "text";
	public static final String COLOR = "color";
	
	//CREATE TABLES
    
	private final static String SQL_CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES +
	" (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	" [" + TITLE + "] TEXT, " +
	" [" + TEXT + "] TEXT, " +
	" [" + COLOR + "] INTEGER" +
	");";
	
	private final static String SQL_CREATE_TABLE_SUBJECTS = "CREATE TABLE " + TABLE_SUBJECTS +
	" (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	" [" + NAME + "] TEXT, " +
	" [" + CAB + "] TEXT, " +
	" [" + HOMEWORK + "] TEXT, " + 
	" [" + DAY + "] INTEGER" + 
	");";
	
	private final static String SQL_CREATE_TABLE_BELLS = "CREATE TABLE " + TABLE_BELLS +
	" (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	" [" + DAY + "] INTEGER, " +
	" [" + START + "] INTEGER, " +
	" [" + END + "] INTEGER" + 
	");";
	
	//DROP TABLES
	private static final String SQL_DELETE_SUBJECTS = "DROP TABLE IF EXISTS " + TABLE_SUBJECTS;
	private static final String SQL_DELETE_NOTES = "DROP TABLE IF EXISTS " + TABLE_NOTES;
	private static final String SQL_DELETE_BELLS = "DROP TABLE IF EXISTS " + TABLE_BELLS;
	
	//DB INFO
	private static final String DB_NAME = "schedule.db";
	private static final int DB_VERSION = 20;
	
	private static DatabaseHelper instance;
	
    public DatabaseHelper() {
        super(AppGlobal.context, DB_NAME, null, DB_VERSION);
    }
	
	public static synchronized DatabaseHelper get() {
		if (instance == null) 
			instance = new DatabaseHelper();
			
		return instance;
	}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SUBJECTS);
        db.execSQL(SQL_CREATE_TABLE_NOTES);
        db.execSQL(SQL_CREATE_TABLE_BELLS);
	}
	
	public static void dropTables(SQLiteDatabase db) {
		db.execSQL(SQL_DELETE_SUBJECTS);
		db.execSQL(SQL_DELETE_NOTES);
		db.execSQL(SQL_DELETE_BELLS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old, int new_version) {
		AppGlobal.saveData();
		
		dropTables(db);
		onCreate(db);
		
		AppGlobal.preferences.edit().putBoolean("is_db_updated", true).apply();
	}
}
