package com.example.flextube.video

import android.os.Build
import androidx.annotation.RequiresApi


class Video(
    val id: String,
    val urlPhoto: String,
    var duration: String,
    val title: String,
    val creatorLogo: String,
    val channelName: String,
    val viewCount: String,
    val likeCount: String,
    val commentCount: String,
    var publishedDate: String
    ) {
    // Przekształcanie na normalne formaty
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            convertDuration()
        }
        convertPublishedAt()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDuration(){

        duration = humanReadableDuration(duration)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun humanReadableDuration(s: String): String = java.time.Duration.parse(s).toString()
        .substring(2).toLowerCase().replace(Regex("[hms](?!\$)")) { "${it.value} " }

    private fun convertPublishedAt(){
        publishedDate = publishedDate.subSequence(0,publishedDate.indexOf('T')).toString()
    }

}