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
            val embedHtml: String,
            @SerializedName("embedHeight")
            val embedHeight: Long,
            @SerializedName("embedWidth")
            val embedWidth: Long
        )
        data class Statistics(
            @SerializedName("likeCount")
            val likeCount: String,

            @SerializedName("dislikeCount")
            val dislikeCount: Long,

            @SerializedName("channelId")
            val channelId: String
        ){}

        data class Snippet(
            @SerializedName("channelTitle")
            val channelTitle: String,

            @SerializedName("channelId")
            val channelId: String,

            @SerializedName("thumbnails")
            val thumbnails: Thumbnails,

            @SerializedName("title")
            val title: String,
        ) {
            data class Thumbnails(
                @SerializedName("medium")
                val picture: Picture
            ){
                data class Picture(
                    @SerializedName("url")
                    val url: String
                ) {}

            }

        }
    }
}
