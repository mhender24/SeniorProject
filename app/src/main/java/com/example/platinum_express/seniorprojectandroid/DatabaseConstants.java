package com.example.platinum_express.seniorprojectandroid;

import android.provider.BaseColumns;

/**
 * Created by marcelhenderson on 10/24/16.
 */

public final class DatabaseConstants {
    private DatabaseConstants() { }

    public static class TimesheetEntry implements BaseColumns {
        public static final String TABLE_NAME = "TimeSheet";

        public static final String COLUMN_NAME_PROCESS = "Process";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_BOARDS = "NumberOfBoards";
        public static final String COLUMN_NAME_HOURS = "Hours";
        public static final String COLUMN_NAME_TASK = "Task";
        public static final String COLUMN_NAME_TASKCOMMENTS = "TaskComments";

        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TimesheetEntry.TABLE_NAME + " (" +
                        TimesheetEntry._ID + " INTEGER PRIMARY KEY," +
                        TimesheetEntry.COLUMN_NAME_PROCESS + " VARCHAR(20) " + COMMA_SEP +
                        TimesheetEntry.COLUMN_NAME_ID + " INT(6) " + COMMA_SEP +
                        TimesheetEntry.COLUMN_NAME_DATE + " DATE " + COMMA_SEP +
                        TimesheetEntry.COLUMN_NAME_BOARDS + " INT " + COMMA_SEP +
                        TimesheetEntry.COLUMN_NAME_HOURS + " INT " + COMMA_SEP +
                        TimesheetEntry.COLUMN_NAME_TASK + " VARCHAR(20) " + COMMA_SEP +
                        TimesheetEntry.COLUMN_NAME_TASKCOMMENTS + " TEXT  )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TimesheetEntry.TABLE_NAME;
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "User_List";

        public static final String COLUMN_NAME_USERID = "User_ID";
        public static final String COLUMN_NAME_FIRSTNAME = "User_First_Name";
        public static final String COLUMN_NAME_USERNAME = "User_Username";
        public static final String COLUMN_NAME_PASSWORD = "User_Password";

        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                        UserEntry.COLUMN_NAME_USERID + " INT(6) PRIMARY KEY " + COMMA_SEP +
                        UserEntry.COLUMN_NAME_FIRSTNAME + " VARCHAR(50) " + COMMA_SEP +
                        UserEntry.COLUMN_NAME_USERNAME + " VARCHAR(50) " + COMMA_SEP +
                        UserEntry.COLUMN_NAME_PASSWORD + " VARCHAR(120) )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
    }
}


