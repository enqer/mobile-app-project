package com.example.flextube.shorts

import com.example.flextube.video.AuthorApiModel
import com.google.gson.annotations.SerializedName

data class ShortsAuthorApiModel(
    @SerializedName("items")
    val items: List<Author>
)
data class Author
    (
    @SerializedName("id")
    val id: String,

    @SerializedName("snippet")
    val snippet: Snippet,

    @SerializedName("statistics")
    val statistics: Statistics
){
    data class Statistics
        (
        @SerializedName("subscriberCount")
        val subscriberCount: String
    ){}

    data class Snippet
        (
        @SerializedName("title")
        val title: String,

        @SerializedName("thumbnails")
        val thumbnails: Thumbnails
    ) {
        data class Thumbnails
            (
            @SerializedName("medium")
            val picture: Picture
        ){
            data class Picture
                (
                @SerializedName("url")
                val url: String
            )
        }
    }
}


