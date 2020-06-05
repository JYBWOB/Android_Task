package com.example.myapplication.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.ui.home.Course;

import java.util.ArrayList;

public class SQLManager {
    private DatabaseHelper databaseHelper;

    public SQLManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void addUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(
                "insert into accounts(username, password) " + "values(?, ?)",
                new String[] {username, password}
        );
    }

    public boolean isExist(String username, String password) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select * from accounts where username = ? and password = ? ",
                new String[] {username, password}
        );
        if(cursor.getCount() > 0)
            return true;
        return false;
    }

    public void addCourse(String username, Course course) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(
                "insert into courses(username, course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?, ?)",
                new String[] {
                        username,
                        course.getCourseName(),
                        course.getTeacher(),
                        course.getClassRoom(),
                        course.getDay()+"",
                        course.getStart()+"",
                        course.getEnd()+""}
        );
    }

    public ArrayList<Course> getCourses(String username) {
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses where username=" + username, null);
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return coursesList;
    }

    public void delCourse(String username, Course course) {
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(
            "delete from courses where username = ? and course_name = ?",
            new String[] {username, course.getCourseName()}
        );
    }

    public void delAutoLogin() {
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(
                "delete from autoLogin"
        );
    }

    public void setAutoLogin(String username) {
        delAutoLogin();
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(
                "insert into autoLogin(username) values(" + username + ")"
        );
    }

    public String getAutoLogin() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from autoLogin", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("username"));
        }
        cursor.close();
        return "N";
    }
}
