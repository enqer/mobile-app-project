package com.example.flextube.playlist_item

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flextube.R


class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        Log.d(TAG,"PLaylistActivity/onCreate -> Start activity")

        val intent = intent
        val message = intent.getStringExtra("message")
        Log.d(message, "message")







    }
}