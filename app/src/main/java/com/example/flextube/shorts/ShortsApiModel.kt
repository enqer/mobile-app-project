package com.example.flextube.video

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ShortsApiModel(
    @SerializedName("items")
    val items: List<ShortsItem>
){
    data class ShortsItem(
        @SerializedName("id")
        val id: String,

        @SerializedName("snippet")
        val snippet: Snippet,

        @SerializedName("statistics")
        val statistics: Statistics

    ) {
        data class Statistics(
            @SerializedName("likeCount")
            val likeCount: Long,

            @SerializedName("dislikeCount")
            val dislikeCount: Long,

            @SerializedName("channelId")
            val channelId: String
        ){}

        data class Snippet(
            @SerializedName("channelTitle")
            val channelTitle: String,

            @SerializedName("thumbnails")
            val thumbnails: Thumbnails,

            @SerializedName("title")
            val title: String
        ) {
            data class Thumbnails(
                @SerializedName("medium")
                val photoVideo: PhotoVideo
            ){
                data class PhotoVideo(
                    @SerializedName("url")
                    val urlPhoto: String
                ) {}
            }

        }
    }
}
