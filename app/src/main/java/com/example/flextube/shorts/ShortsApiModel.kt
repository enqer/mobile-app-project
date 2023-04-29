package com.example.flextube.shorts

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ShortsApiModel(
    @SerializedName("items")
    val items: List<Shorts>
){
    data class Shorts(
        @SerializedName("id")
        val id: String,

        @SerializedName("snippet")
        val snippet: Snippet,

        @SerializedName("statistics")
        val statistics: Statistics,
        @SerializedName("player")
        val player: Player

    ) {
        data class Player(
            @SerializedName("embedHtml")
            val embedHtml: String
        )
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
