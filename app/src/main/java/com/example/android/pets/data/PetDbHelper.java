package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.data.PetContract.PetEntry;


public class PetDbHelper extends SQLiteOpenHelper {
    //If you change the database schema, you must implement the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Shelter.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +
                    PetEntry.TABLE_NAME + " (" +
                    PetEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    PetEntry.COLUMN_PET_NAME + TEXT_TYPE + COMMA_SEP +
                    PetEntry.COLUMN_PET_BREED + TEXT_TYPE + COMMA_SEP +
                    PetEntry.COLUMN_PET_GENDER + INTEGER_TYPE + COMMA_SEP +
                    PetEntry.COLUMN_PET_WEIGHT + INTEGER_TYPE + " );";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS" + PetEntry.TABLE_NAME;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This database in only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

