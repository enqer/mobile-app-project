package com.example.flextube.video

import android.graphics.Picture
import com.google.gson.annotations.SerializedName

data class AuthorApiModel
    (
    @SerializedName("items")
    val items: List<Items>
    ) {
        data class Items
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

    }