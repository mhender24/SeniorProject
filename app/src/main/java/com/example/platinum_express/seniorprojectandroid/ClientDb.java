package com.example.platinum_express.seniorprojectandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Date;

import static android.R.attr.process;
import static com.example.platinum_express.seniorprojectandroid.R.id.date;

/**
 * Created by marcelhenderson on 10/24/16.
 */

public class ClientDb {
    SQLiteOpenHelper helper;

    public ClientDb(Context context){
        helper = new DatabaseHelper(context);
    }

    Cursor getTimeSheetRecordsForUser()
    {
        Cursor cursor = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            cursor = db.query(DatabaseConstants.TimesheetEntry.TABLE_NAME, null, null, null, null, null, null);
            //  https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
       }
        catch (SQLException e) {
            Log.d("Mine", "Query error: " + e);
        }
        return cursor;
    }

    void insertIntoTimesheet(String process, int id, Date date, int boards, int hours, String task, String comment)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();

            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_PROCESS, process);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_ID, id);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_DATE, date.toString());
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_BOARDS, boards);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_HOURS, hours);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_TASK, task);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_TASK, comment);

            db.insert(DatabaseConstants.TimesheetEntry.TABLE_NAME, null, values);
        }
        catch (SQLException e)
        {
            Log.d("Mine", "Insert error: "+ e);
        }
    }

    void testInsertIntoTimesheet(String process, String date, int boards, int hours, String task)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_PROCESS, process);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_DATE, date);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_BOARDS, boards);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_HOURS, hours);
            values.put(DatabaseConstants.TimesheetEntry.COLUMN_NAME_TASK, task);

            db.insert(DatabaseConstants.TimesheetEntry.TABLE_NAME, null, values);
        }
        catch (SQLException e)
        {
            Log.d("Mine", "Insert error: "+ e);
        }
    }

    void insertIntoUsers(int id, String firstName, String username, String password){
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.UserEntry.COLUMN_NAME_USERID, id);
            values.put(DatabaseConstants.UserEntry.COLUMN_NAME_FIRSTNAME, firstName);
            values.put(DatabaseConstants.UserEntry.COLUMN_NAME_USERNAME, username);
            values.put(DatabaseConstants.UserEntry.COLUMN_NAME_PASSWORD, password);

            db.insert(DatabaseConstants.TimesheetEntry.TABLE_NAME, null, values);
        }
        catch (SQLException e)
        {
            Log.d("Mine", "Insert error: "+ e);
        }
    }
}
