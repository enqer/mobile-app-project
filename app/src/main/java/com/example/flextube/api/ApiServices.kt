package com.example.flextube.api

import com.example.flextube.comment.CommentApiModel
import com.example.flextube.video.AuthorApiModel
import com.example.flextube.video.VideoApiModel
import com.example.flextube.video.VideoIdsApiModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    // returns image date and name of channel
    @GET("search")
    fun getVideos(
        @Query("part") part: String = "snippet",
        @Query("key") key: String = KEY,
        @Query("type") type: String = "video",  // musi być ustawione jeśli chcemy videoEmbeddable
//        @Query("videoEmbeddable") videoEmbeddable: String = "true",  // dzięki temu wyszukujemy filmy tylko dostępne dla stron trzecich
        @Query("maxResults") results: Int = 10  // 10 filmów się wyświetli na głównej tylko defaultowo jest 5 na api
    ) : Call<VideoIdsApiModel>

    // return views depends on id
    @GET("videos")
    fun getStatsVideos(
        @Query("part") part: String = "contentDetails",
        @Query("part") part2: String = "statistics",
        @Query("part") part3: String = "snippet",
        @Query("part") part4: String = "player",
        @Query("id") id: String,
        @Query("key") key: String = KEY
    ) : Call<VideoApiModel>

    @GET("channels")
    fun getChannel(
        @Query("part") part: String = "snippet",
        @Query("part") part2: String = "statistics",
        @Query("id") id: String,
        @Query("key") key: String = KEY
    ) : Call<AuthorApiModel>

    @GET("commentThreads")
    fun getCommentsOfVideo(
        @Query("part") part: String ="snippet",
        @Query("order") order: String = "relevance", // najpopularniejsze
        @Query("videoId") videoId: String,
        @Query("maxResults") results: Int = 10,
        @Query("key") key: String = KEY
    ) : Call<CommentApiModel>





    companion object {
        private final const val KEY = "AIzaSyBaUPRMqZMOs8drD14sw25bCDD5QFHi6Cw"
        private final const val KEY2 = "AIzaSyBVhdqkI4hsX2iJDyicTQxQqPrk7b4jYTk"
        fun getRetrofit(): ApiServices {
            val retrofit: Retrofit =
                Retrofit.Builder().baseUrl("https://www.googleapis.com/youtube/v3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(ApiServices::class.java)
        }
    }
}