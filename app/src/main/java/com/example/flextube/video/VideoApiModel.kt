package com.example.flextube.video

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VideoApiModel(
    @SerializedName("items")
    val items: List<VideoItem>
){
    data class VideoItem(
        @SerializedName("id")
        val id: String,

        @SerializedName("snippet")
        val snippet: Snippet,

        @SerializedName("contentDetails")
        val contentDetails: ContentDetails,

        @SerializedName("statistics")
        val statistics: Statistics


    ) {
        data class ContentDetails(
            @SerializedName("duration")
            val duration: String
        ){}
        data class Statistics(
            @SerializedName("viewCount")
            val viewCount: String,

            @SerializedName("likeCount")
            val likeCount: String,

            @SerializedName("commentCount")
            val commentCount: String

        ){}
        data class Snippet(
            @SerializedName("publishedAt")
            val publishedAt: String,

            @SerializedName("channelId")
            val channelId: String,

            @SerializedName("title")
            val title: String,

            @SerializedName("channelTitle")
            val channelTitle: String,

            @SerializedName("thumbnails")
            val thumbnails: Thumbnails
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