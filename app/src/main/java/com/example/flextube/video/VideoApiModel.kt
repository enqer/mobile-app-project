package com.example.flextube.video

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VideoApiModel(
    @SerializedName("items")
    val items: List<VideoItem>
){

    data class VideoItem(
        @SerializedName("snippet")
        val snippet: Snippet
    ) {
        data class Snippet(
            @SerializedName("publishedAt")
            val publishedAt: String,

            @SerializedName("title")
            val title: String,

            @SerializedName("thumbnails")
            val thumbnails: Thumbnails
        ) {
            data class Thumbnails(
                @SerializedName("high")
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