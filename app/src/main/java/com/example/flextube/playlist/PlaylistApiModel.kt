package com.example.flextube.playlist

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PlaylistApiModel(
    @SerializedName("nextPageToken")
    val nextPageToken: String?,

    @SerializedName("items")
    val items: List<PlaylistItem>

    ) {
    data class PlaylistItem(
        @SerializedName("id")
        val id: String,

        @SerializedName("snippet")
        val snippetYt: SnippetYt,

        @SerializedName("contentDetails")
        val contentDetail: ContentDetail
    )

    data class SnippetYt(
        @SerializedName("title")
        val title: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("customUrl")
        val customUrl: String,

        @SerializedName("publishedAt")
        val publishedAt: String,

        @SerializedName("thumbnails")
        val thumbnails: ThumbnailsYt,

        @SerializedName("country")
        val country: String

    )
    data class ThumbnailsYt(
        @SerializedName("high")
        val high: High
    ) {
        data class High(
            @SerializedName("url")
            val url: String
        )
    }

    data class ContentDetail(
        @SerializedName("itemCount")
        val itemCount: Int
    )

}