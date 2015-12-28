package com.vantuz.video_extractor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
public class HistoryDBSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_FILE_NAME = "history.db";
    private static final int DB_VERSION_1 = 1;
    private static volatile HistoryDBSQLiteOpenHelper instance;
    private final Context context;

    public static final String TABLE_HISTORY = "history";
    public static final String URL = "url";

    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE " + TABLE_HISTORY
            + " ("
            + URL + " TEXT"
            + " )";

    public HistoryDBSQLiteOpenHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION_1);
        this.context = context.getApplicationContext();
    }

    public static HistoryDBSQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (HistoryDBSQLiteOpenHelper.class) {
                if (instance == null) {
                    instance = new HistoryDBSQLiteOpenHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate: " + CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "onUpgrade: oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }

    public void dropDb() {
        SQLiteDatabase db = getWritableDatabase();
        if (db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                Log.w(LOG_TAG, "Failed to close DB");
            }
        }
        final File dbFile = context.getDatabasePath(DB_FILE_NAME);
        try {
            Log.d(LOG_TAG, "deleting the database file: " + dbFile.getPath());
            if (!dbFile.delete()) {
                Log.w(LOG_TAG, "Failed to delete database file: " + dbFile);
            }
            Log.d(LOG_TAG, "Deleted DB file: " + dbFile);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Failed to delete database file: " + dbFile, e);
        }
    }

    private static final String LOG_TAG = "HistoryDB";
}