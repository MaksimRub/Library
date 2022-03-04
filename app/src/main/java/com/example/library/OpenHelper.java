package com.example.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {

    public  static final String DATABASE_NAME="library.db";
    public  static  final int DATABASE_VERSION=1;
    public static final String TABLE_NAME ="books";
    public static final String COLUMN_AUTHOR ="author";
    public static final String COLUMN_TITLE ="title";
    public static final String COLUMN_GENRE ="genre";
    public static final String COLUMN_YEAR ="year";

    public OpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery="CREATE TABLE "+TABLE_NAME+"(_id integer PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_AUTHOR+" TEXT, "+
                COLUMN_TITLE+" TEXT NOT NULL, "+
                COLUMN_GENRE+"TEXT, "+
                COLUMN_YEAR+" integer);";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }
}
