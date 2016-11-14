package com.example.platinum_express.seniorprojectandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcelhenderson on 10/24/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_FILENAME="SeniorProject.db";
    private static final String dbClassName = "com.mysql.jdbc.Driver";
    private static final String CONNECTION = "jdbc:mysql://50.63.236.101/BGMTracking";
    private static final int DATABASE_VERSION=1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table people (name text, age integer, height real);
        db.execSQL(DatabaseConstants.TimesheetEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseConstants.UserEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseConstants.TimesheetEntry.SQL_CREATE_TABLE);
        db.execSQL(DatabaseConstants.UserEntry.SQL_CREATE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
