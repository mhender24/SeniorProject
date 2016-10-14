package com.example.platinum_express.seniorprojectandroid; /**
 * Created by marcelhenderson on 10/14/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbCreate extends SQLiteOpenHelper{

    private static final String Database_filename="SeniorProject.db";
    private static final int Database_version=1;
    SQLiteOpenHelper helper;


    public DbCreate(Context context) {
        super(context, Database_filename, null, Database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table people (name text, age integer, height real);
        String sql = "CREATE TABLE TimeSheet(" +
                "id int primary key auto_increment not null," +
                "Process varchar(20),"+
                "ID int(6),"+
                "Date DATE,"+
                "NumberOfBoards int,"+
                "Hours int,"+
                "Task varchar(20),"+
                "TaskComments text"+
                "Foreign key(ID) references User_List(User_ID));"+
                "Create Table User_List("+
                "User_ID int(6) not null Primary key"+
                "User_First_Name varchar(50) not null"+
                "User_Username varchar(50) not null"+
                "User_Password varchar(128) not null);";
        db.execSQL(sql);
    }

    public void insert(SQLiteDatabase db){
        String sql = "Insert into User_List(User_ID, User_First_Name, User_Username, User_Password) " +
                "values(1, Joe, Schmo, jschmo, password);";
        db.execSQL(sql);

    }

    public Cursor get(SQLiteDatabase db){
        Cursor cursor = null;
        try
        {
            cursor = db.query("User_List", null, null, null, null, null, "Select * from User_List");
        }
        catch (SQLException e)
        {
            Log.d("Mine", "Query error: "+ e);
        }

        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE ");
        onCreate(db);
    }
}
