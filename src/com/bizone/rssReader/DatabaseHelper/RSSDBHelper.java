package com.bizone.rssReader.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

class RSSDBHelper extends SQLiteOpenHelper {
    public RSSDBHelper(Context context) {
	super(context, "simple_rss_reader.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
	db.execSQL("CREATE TABLE feeds ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
		   + "url VARCHAR(255) UNIQUE NOT NULL, name VARCHAR(255) DEFAULT '');");
	db.execSQL("CREATE TABLE articles ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
		   + "url VARCHAR(255) UNIQUE NOT NULL);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE ID EXISTS feeds");
	db.execSQL("DROP TABLE ID EXISTS articles");
	onCreate(db);
    }
}