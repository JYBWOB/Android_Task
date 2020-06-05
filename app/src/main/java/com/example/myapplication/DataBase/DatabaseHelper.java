package com.example.myapplication.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.ui.home.Course;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table accounts(" +
                "username text primary key," +
                "password text)");

        db.execSQL("create table courses(" +
                "username text," +
                "course_name text," +
                "teacher text," +
                "class_room text," +
                "day integer," +
                "class_start integer," +
                "class_end integer," +
                "foreign key (username) " +
                "references accounts(username))");
    }
}
