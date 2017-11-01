package com.example.ritika.onlylocation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ritika.onlylocation.MarkersContract;

/**
 * Created by RITIKA on 18-04-2017.
 */

public class MarkersDbHelper extends SQLiteOpenHelper{


    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME = "Markers.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MarkersContract.MarkersEntry.TABLE_NAME
            + " (" + MarkersContract.MarkersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MarkersContract.MarkersEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
            MarkersContract.MarkersEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +MarkersContract.MarkersEntry.COLUMN_LATITUDE +TEXT_TYPE + COMMA_SEP +MarkersContract.MarkersEntry.COLUMN_LONGITUDE +TEXT_TYPE + COMMA_SEP + MarkersContract.MarkersEntry.COLUMN_ADDRESS +TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MarkersContract.MarkersEntry.TABLE_NAME;

    public MarkersDbHelper(Context context)
    {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }
}
