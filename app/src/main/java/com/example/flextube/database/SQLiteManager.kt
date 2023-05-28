package com.example.flextube.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.flextube.video.AuthorVideo
import com.example.flextube.video.Video


class SQLiteManager(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "flextubeDB"
        private const val TABLE_NAME = "table_flextube"
        private const val DATABASE_VERSION = 1
        private const val VIDEO_ID = "video_id"
        private const val URL_PHOTO = "urlPhoto"
        private const val DURATION = "duration"
        private const val TITLE = "title"
        private const val VIEW_COUNT = "view_count"
        private const val LIKE_COUNT = "like_count"
        private const val COMMENT_COUNT = "comment_count"
        private const val PUBLISHED_DATE = "published_date"
        private const val PLAYER_HTML = "player_html"
        private const val PLAYER_HEIGHT = "player_height"
        private const val PLAYER_WIDTH = "player_width"
        private const val AUTHOR_ID = "author_id"
        private const val AUTHOR_NAME = "author_name"
        private const val AUTHOR_URL_LOGO = "author_url_logo"
        private const val AUTHOR_SUBSCRIBERS = "author_subscribers"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE "+ TABLE_NAME + "("
                + VIDEO_ID + " TEXT PRIMARY KEY,"
                + URL_PHOTO + " TEXT," + DURATION + " TEXT,"
                + TITLE + " TEXT," + VIEW_COUNT + " TEXT,"
                + LIKE_COUNT + " TEXT," +
                COMMENT_COUNT + " TEXT," + PUBLISHED_DATE + " TEXT,"
                + PLAYER_HTML + " TEXT," + PLAYER_HEIGHT + " LONG,"
                + PLAYER_WIDTH + " LONG," + AUTHOR_ID + " TEXT,"
                + AUTHOR_NAME + " TEXT," + AUTHOR_URL_LOGO + " TEXT,"
                + AUTHOR_SUBSCRIBERS + " TEXT" +")")
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertVideo(video: Video): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(VIDEO_ID, video.id)
        contentValues.put(URL_PHOTO, video.urlPhoto)
        contentValues.put(DURATION, video.duration)
        contentValues.put(TITLE, video.title)
        contentValues.put(VIEW_COUNT, video.viewCount)
        contentValues.put(LIKE_COUNT, video.likeCount)
        contentValues.put(COMMENT_COUNT, video.commentCount)
        contentValues.put(PUBLISHED_DATE, video.publishedDate)
        contentValues.put(PLAYER_HTML, video.playerHtml)
        contentValues.put(PLAYER_HEIGHT, video.playerHeight)
        contentValues.put(PLAYER_WIDTH, video.playerWidth)
        contentValues.put(AUTHOR_ID, video.authorVideo.id)
        contentValues.put(AUTHOR_NAME, video.authorVideo.name)
        contentValues.put(AUTHOR_URL_LOGO, video.authorVideo.urlLogo)
        contentValues.put(AUTHOR_SUBSCRIBERS, video.authorVideo.subscriberCount)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }
    @SuppressLint("Range")
    fun getVideosHistory(): ArrayList<Video> {
        val videoList: ArrayList<Video> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var video_id: String
        var url_photo: String
        var duration: String
        var title: String
        var view_count: String
        var like_count: String
        var comment_count: String
        var published_date: String
        var player_html: String
        var player_height: Long
        var player_width: Long
        var author_id: String
        var author_name: String
        var author_url_logo: String
        var author_subs: String

        if (cursor.moveToFirst()){
            do{
                video_id = cursor.getString(cursor.getColumnIndex(VIDEO_ID))
                url_photo = cursor.getString(cursor.getColumnIndex(URL_PHOTO))
                duration = cursor.getString(cursor.getColumnIndex(DURATION))
                title = cursor.getString(cursor.getColumnIndex(TITLE))
                view_count = cursor.getString(cursor.getColumnIndex(VIEW_COUNT))
                like_count = cursor.getString(cursor.getColumnIndex(LIKE_COUNT))
                comment_count = cursor.getString(cursor.getColumnIndex(COMMENT_COUNT))
                published_date = cursor.getString(cursor.getColumnIndex(PUBLISHED_DATE))
                player_html = cursor.getString(cursor.getColumnIndex(PLAYER_HTML))
                player_height = cursor.getLong(cursor.getColumnIndex(PLAYER_HEIGHT))
                player_width = cursor.getLong(cursor.getColumnIndex(PLAYER_WIDTH))
                author_id = cursor.getString(cursor.getColumnIndex(AUTHOR_ID))
                author_name = cursor.getString(cursor.getColumnIndex(AUTHOR_NAME))
                author_url_logo = cursor.getString(cursor.getColumnIndex(AUTHOR_URL_LOGO))
                author_subs = cursor.getString(cursor.getColumnIndex(AUTHOR_SUBSCRIBERS))

                val author = AuthorVideo(author_id,author_name,author_url_logo,author_subs)
                val v = Video(video_id,url_photo,duration,title,view_count,like_count,comment_count,published_date,player_html,player_height,player_width, author)

                videoList.add(v)

            } while (cursor.moveToNext())
        }
        return videoList
    }
}