package com.example.flextube.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS my_table8 (id INTEGER PRIMARY KEY, video_id TEXT, urlPhotoValue TEXT, titleValue TEXT,  authorVideo TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS my_table8")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "my_database"
        private const val DATABASE_VERSION = 8
    }
}
