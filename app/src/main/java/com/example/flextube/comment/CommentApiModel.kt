package com.example.flextube.comment

import com.google.gson.annotations.SerializedName

data class CommentApiModel
    (
    @SerializedName("items")
    val items: List<Items>
            )
{
        data class Items
            (
            @SerializedName("snippet")
            val snippet: Snippet
                    )
        {
                data class Snippet
                    (
                    @SerializedName("topLevelComment")
                    val topLevelComment: TopLevelComment
                            ){
                        data class TopLevelComment
                            (
                            @SerializedName("id")
                            val id: String,

                            @SerializedName("snippet")
                            val snip: Snip
                                    ){
                                data class Snip
                                    (
                                        @SerializedName("textDisplay")
                                        val textDisplay: String,

                                        @SerializedName("authorDisplayName")
                                        val authorDisplayName: String,

                                        @SerializedName("authorProfileImageUrl")
                                        val authorProfileImageUrl: String,

                                        @SerializedName("likeCount")
                                        val likeCount: Int,

                                        @SerializedName("publishedAt")
                                        val publishedAt: String
                                            )
                            }
                    }
            }

}