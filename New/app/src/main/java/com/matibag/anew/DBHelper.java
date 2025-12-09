package com.matibag.anew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Renamed helper class to DBHelper to avoid conflict with android.database.sqlite.SQLiteDatabase
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "records.db";
    public static final String PROFILE = "profile";
    public static final String PROFILE_ID = "id";
    public static final String PROFILE_FNAME = "fname";
    public static final String PROFILE_MNAME = "mname";
    public static final String PROFILE_LNAME = "lname";
    public static final String PROFILE_ADDRESS = "address";
    public static final String PROFILE_EMAIL = "email";

    public ArrayList<String> Records;
    public ArrayList<Integer> RecordsId;
    public String sql = null;
    public Cursor rs;
    public ContentValues Values;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // version 2 as original
    }

    @Override
    public void onCreate(SQLiteDatabase conn) {
        conn.execSQL("CREATE TABLE IF NOT EXISTS " + PROFILE + " (" + PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PROFILE_FNAME + " TEXT, " + PROFILE_MNAME + " TEXT, " + PROFILE_LNAME + " TEXT, "
                + PROFILE_ADDRESS + " TEXT, " + PROFILE_EMAIL + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase conn, int oldVersion, int newVersion) {
        conn.execSQL("DROP TABLE IF EXISTS " + PROFILE);
        onCreate(conn);
    }

    public boolean AddRecords(String fname, String mname, String lname, String address, String email) {
        SQLiteDatabase conn = this.getWritableDatabase();
        try {
            Values = new ContentValues();
            Values.put(PROFILE_FNAME, fname);
            Values.put(PROFILE_MNAME, mname);
            Values.put(PROFILE_LNAME, lname);
            Values.put(PROFILE_ADDRESS, address);
            Values.put(PROFILE_EMAIL, email);
            conn.insert(PROFILE, null, Values);
            return true;
        } finally {
            conn.close();
        }
    }

    /**
     * Update names + address/email
     */
    public boolean UpdateRecords(String fname, String mname, String lname, String address, String email, Integer id) {
        SQLiteDatabase conn = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(PROFILE_FNAME, fname);
            cv.put(PROFILE_MNAME, mname);
            cv.put(PROFILE_LNAME, lname);
            cv.put(PROFILE_ADDRESS, address);
            cv.put(PROFILE_EMAIL, email);
            int rows = conn.update(PROFILE, cv, PROFILE_ID + " = ?", new String[]{String.valueOf(id)});
            return rows > 0;
        } finally {
            conn.close();
        }
    }

    /**
     * Update only name fields (overload used by UpdateActivity which doesn't collect address/email)
     */
    public boolean UpdateRecords(String fname, String mname, String lname, Integer id) {
        SQLiteDatabase conn = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(PROFILE_FNAME, fname);
            cv.put(PROFILE_MNAME, mname);
            cv.put(PROFILE_LNAME, lname);
            int rows = conn.update(PROFILE, cv, PROFILE_ID + " = ?", new String[]{String.valueOf(id)});
            return rows > 0;
        } finally {
            conn.close();
        }
    }

    public Cursor getData(Integer id) {
        SQLiteDatabase conn = this.getReadableDatabase();
        // caller should manage cursor lifecycle; do NOT close db until cursor used
        rs = conn.rawQuery("SELECT * FROM " + PROFILE + " WHERE " + PROFILE_ID + " = ?", new String[]{String.valueOf(id)});
        return rs;
    }

    public boolean ClearRecord() {
        SQLiteDatabase conn = this.getWritableDatabase();
        try {
            conn.execSQL("DELETE FROM " + PROFILE);
            return true;
        } finally {
            conn.close();
        }
    }

    public boolean DeleteRecord(Integer id) {
        SQLiteDatabase conn = this.getWritableDatabase();
        try {
            int rows = conn.delete(PROFILE, PROFILE_ID + " = ?", new String[]{String.valueOf(id)});
            return rows > 0;
        } finally {
            conn.close();
        }
    }

    public ArrayList<String> GetAllData() {
        SQLiteDatabase conn = this.getReadableDatabase();
        Records = new ArrayList<>();
        RecordsId = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = conn.rawQuery("SELECT * FROM " + PROFILE, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(PROFILE_ID));
                    RecordsId.add(id);

                    String entry = cursor.getString(cursor.getColumnIndex(PROFILE_LNAME)) + ", "
                            + cursor.getString(cursor.getColumnIndex(PROFILE_FNAME)) + " "
                            + cursor.getString(cursor.getColumnIndex(PROFILE_MNAME)) + " | "
                            + cursor.getString(cursor.getColumnIndex(PROFILE_EMAIL)) + " | "
                            + cursor.getString(cursor.getColumnIndex(PROFILE_ADDRESS));
                    Records.add(entry);
                } while (cursor.moveToNext());
            }
            return Records;
        } finally {
            if (cursor != null) cursor.close();
            conn.close();
        }
    }
}