package com.example.flextube.playlist_item

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PlaylistItemApiModel(
    @SerializedName("items")
    val items: List<PlaylistItem>

){
    data class PlaylistItem(
        @SerializedName("id")
        val id: String,

        @SerializedName("snippet")
        val snippet: Snippet

    ){
        data class Snippet(
            @SerializedName("publishedAt")
            val publishedAt: String,

            @SerializedName("channelId")
            val channelId: String,

            @SerializedName("title")
            val title: String,

            @SerializedName("thumbnails")
            val thumbnails: Thumnails,

            @SerializedName("videoOwnerChannelTitle")
            val videoOwnerChannelTitle: String,

            @SerializedName("videoOwnerChannelId")
            val videoOwnerChannelId: String,

            @SerializedName("playlistId")
            val playlistId: String,

            @SerializedName("resourceId")
            val resourceId: ResourceId

        ){
            data class Thumnails(
                @SerializedName("medium")
                val medium: Medium
            ) {
                data class Medium(
                    @SerializedName("url")
                    val url: String
                )
            }

            data class ResourceId(
                @SerializedName("videoId")
                val videoId: String
            )
        }
    }
    data class ContentDetails(
        @SerializedName("videoId")
        val videoId: String
    )
}