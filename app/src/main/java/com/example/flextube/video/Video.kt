package com.example.flextube.video

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration


class Video(
    val id: String,
    val urlPhoto: String,
    var duration: String,
    val title: String,
    var viewCount: String,
    var likeCount: String,
    val commentCount: String,
    var publishedDate: String,
    val playerHtml: String,
    val playerHeight: Long,
    val playerWidth: Long,
    val authorVideo: AuthorVideo
    ) {

    // Przekształcanie na normalne formaty
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            duration = convertTime(duration)
        }
        convertPublishedAt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTime(time: String): String {
        if (':' in time)
            return time

        val duration = Duration.parse(time)
        val hours = duration.toHours()

        val minutes = duration.minusHours(hours).toMinutes()
        val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else if (hours.toInt() == 0 && minutes.toInt() == 0 && seconds.toInt() == 0) {
            "\uD83D\uDD34 Live"
        } else {
            if (minutes < 10) {
                String.format("%01d:%02d", minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    }
    private fun convertPublishedAt() {
        if(!publishedDate.contains("T"))
            publishedDate = publishedDate + "T"
        publishedDate = publishedDate.subSequence(0,publishedDate.indexOf('T')).toString()
    }
}