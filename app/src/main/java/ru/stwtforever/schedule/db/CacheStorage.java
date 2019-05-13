package ru.stwtforever.schedule.db;


import android.content.*;
import android.database.*;
import android.text.*;
import java.util.*;
import ru.stwtforever.schedule.adapter.items.*;
import static ru.stwtforever.schedule.common.AppGlobal.*;
import static ru.stwtforever.schedule.db.DatabaseHelper.*;

public class CacheStorage {
	
    public static void checkOpen() {
        if (!database.isOpen()) {
            database = DatabaseHelper.get().getWritableDatabase();
        }
    }

    private static Cursor selectCursor(String table, String column, Object value) {
        return QueryBuilder.query()
                .select("*").from(table)
                .where(column.concat(" = ").concat(String.valueOf(value)))
                .asCursor(database);
    }

    private static Cursor selectCursor(String table, String column, int... ids) {
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

    private static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static byte[] getBlob(Cursor cursor, String columnName) {
        return cursor.getBlob(cursor.getColumnIndex(columnName));
    }

	public static ArrayList<SubjectItem> getSubjects(int day) {
		Cursor c = selectCursor(TABLE_SUBJECTS, DAY, day);
		
		ArrayList<SubjectItem> subs = new ArrayList<>(c.getCount());
		while (c.moveToNext()) {
			subs.add(parseSubject(c));
		}
		
		c.close();
		return subs;
	}
	
	public static ArrayList<SubjectItem> getSubjects() {
		Cursor c = selectCursor(TABLE_SUBJECTS);

		ArrayList<SubjectItem> subs = new ArrayList<>(c.getCount());
		while (c.moveToNext()) {
			subs.add(parseSubject(c));
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
	
	public static ArrayList<BellItem> getBells() {
		Cursor c = selectCursor(TABLE_BELLS);
		
		ArrayList<BellItem> items = new ArrayList<>(c.getCount());
		while (c.moveToNext()) {
			items.add(parseBell(c));
		}
		
		c.close();
		return items;
	}
	
	public static ArrayList<BellItem> getBells(int day) {
		Cursor c = selectCursor(TABLE_BELLS, DAY, day);

		ArrayList<BellItem> items = new ArrayList<>(c.getCount());
		while (c.moveToNext()) {
			items.add(parseBell(c));
		}

		c.close();
		return items;
	}
	
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

    public static void insert(String table, ArrayList values) {
        database.beginTransaction();

        ContentValues cv = new ContentValues();
        for (Object item : values) {
            switch (table) {
                case TABLE_NOTES:
					putValues(cv, (NoteItem) item);
					break;
				case TABLE_SUBJECTS:
					putValues(cv, (SubjectItem) item);
					break;
				case TABLE_BELLS:
					putValues(cv, (BellItem) item);
					break;
            }

            database.insert(table, null, cv);
            cv.clear();
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }
	
	public static void update(String table, ArrayList values, String where, Object args) {
		database.beginTransaction();
		
		ContentValues cv = new ContentValues();
        for (int i = 0; i < values.size(); i++) {
            Object item = values.get(i);
            switch (table) {
                case TABLE_NOTES:
					putValues(cv, (NoteItem) item);
					break;
				case TABLE_SUBJECTS:
					putValues(cv, (SubjectItem) item);
					break;
				case TABLE_BELLS:
					putValues(cv, (BellItem) item);
					break;
            }

            database.update(table, cv, where, new String[] {String.valueOf(args)});
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
	
	private static SubjectItem parseSubject(Cursor c) {
		SubjectItem s = new SubjectItem();
		
		s.setId(getInt(c, ID));
		s.setCab(getString(c, CAB));
		s.setDay(getInt(c, DAY));
		s.setColor(getInt(c, COLOR));
		s.setHomework(getString(c, HOMEWORK));
		s.setName(getString(c, NAME));
		
		return s;
	}
	
	private static NoteItem parseNote(Cursor c) {
		NoteItem item = new NoteItem();
		
		item.id = getInt(c, ID);
		item.color = getInt(c, COLOR);
		item.title = getString(c, TITLE);
		item.text = getString(c, TEXT);
		
		return item;
	}
	
	private static BellItem parseBell(Cursor c) {
		BellItem i = new BellItem();
		
		i.id = getInt(c, ID);
		i.start = getInt(c, START);
		i.end = getInt(c, END);
		i.day = getInt(c, DAY);
		
		return i;
	}
	
	private static void putValues(ContentValues cv, NoteItem item) {
		cv.put(TITLE, item.title);
		cv.put(COLOR, item.color);
		cv.put(TEXT, item.text);
	}
	
	private static void putValues(ContentValues cv, SubjectItem item) {
		cv.put(DAY, item.getDay());
		cv.put(CAB, item.getCab());
		cv.put(NAME, item.getName());
		cv.put(COLOR, item.getColor());
		cv.put(HOMEWORK, item.getHomework());
	}
	
	private static void putValues(ContentValues cv, BellItem item) {
		cv.put(START, item.start);
		cv.put(END, item.end);
		cv.put(DAY, item.day);
	}
}
