package com.cts.cheetah.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by manu.palassery on 20-02-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase database = this.getWritableDatabase();
    Context context;

    public static final String DB_NAME = "Cheetah_DB";
    public static final int DB_VERSION = 1;

    //Database Table Names
    public static final String TABLE_SAMPLE = "Sample_Table";

    //Sample Table Field Names
    public static final String SAMPLE_ID = "Sample_ID";
    public static final String SAMPLE_NAME = "Sample_Name";
    public static final String SAMPLE_TYPE = "Sample_Type";

    public static final String SAMPLE_CREATE_TABLE_STATEMENT = "CREATE TABLE " + TABLE_SAMPLE + "(" +

            SAMPLE_ID + " INTEGER," +
            SAMPLE_NAME + " TEXT," +
            SAMPLE_TYPE + " TEXT )";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
