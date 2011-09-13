package org.okcjug.android.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
	public static final String KEY_ROWID = "_id";
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private static DatabaseAdapter instance;

	public static DatabaseAdapter instance(Context ctx) {
		if (instance == null) {
			instance = new DatabaseAdapter(ctx);
			instance.open();
		}

		return instance;
	}

	private DatabaseAdapter(Context ctx) {
		dbHelper = new DatabaseHelper(ctx);
	}

	public DatabaseAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

    public Cursor fetchAllNotes() {
        String query = "SELECT * FROM notes";
        return db.rawQuery(query, null);
    }

    public Cursor getNote(String noteId) {
    	return db.rawQuery("SELECT * FROM notes WHERE _id = ?", new String[]{ noteId }) ;
    }
    
    public long addNote(String name, String body) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("body", body);

        return db.insert("notes", null, cv);
    }
    
    public void updateNote(String id, String name, String body) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("body", body);

        db.update("notes", cv, "_id = ?", new String[] {id});
    }
    
    public void deleteNote(String id) {
    	db.delete("notes", "_id = ?", new String[] {id});
    }
}
