package com.example.flextube.video

import com.google.gson.annotations.SerializedName

data class VideoIdsApiModel (

    @SerializedName("items")
    val items: List<VideoItem>

    )   {
        data class VideoItem(
        @SerializedName("id")
        val id: Id,
        ){
            data class Id(
                @SerializedName("videoId")
                val videoId: String
            ){}
        }

}