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

    data class ContentDetail(
        @SerializedName("itemCount")
        val itemCount: Int
    )
}



