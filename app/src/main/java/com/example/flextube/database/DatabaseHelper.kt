package com.example.flextube.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val databaseVersion: String = "my_table12"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $databaseVersion (id INTEGER PRIMARY KEY, " +
                "video_id TEXT, " +
                "urlPhotoValue TEXT, " +
                "durationValue TEXT, " +
                "titleValue TEXT, " +
                "viewCountValue TEXT, " +
                "likeCountValue TEXT, " +
                "authorVideo TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $databaseVersion")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "my_database"
        private const val DATABASE_VERSION = 12
    }
}
