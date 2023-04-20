package com.example.flextube.video

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

// TODO Pobrać jeszcze info o playerze? może później

@Keep
class VideoStatsApiModel
    (
    @SerializedName("items")
    val items: List<Items>


    ){
    data class Items(
        @SerializedName("contentDetails")
        val contentDetails: ContentDetails,

        @SerializedName("statistics")
        val statistics: Statistics
    ){
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
    }



    }

