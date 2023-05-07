package com.example.flextube.api


import com.example.flextube.playlist.PlaylistApiModel

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
    fun getSearchedVideos(
        @Query("part") part: String = "snippet",
        @Query("key") key: String = KEY,
        @Query("q") q: String,
        @Query("type") type: String = "video", // musi być ustawione jeśli chcemy videoEmbeddable
        @Query("maxResults") maxResults: Int = 20  // 10 filmów się wyświetli na głównej tylko, defaultowo jest 5 na api
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
        @Query("order") order: String = "relevance",
        @Query("videoId") videoId: String,
        @Query("maxResults") results: Int = 40,
        @Query("key") key: String = KEY,
        @Query("textFormat") textFormat: String = "plainText"
    ) : Call<CommentApiModel>
    @GET("playlists")
    fun getPlaylist(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("key") key: String = KEY2,
        @Query("pageToken") pageToken: String?,
        @Query("maxResults") maxResults: Int

    ): Call<PlaylistApiModel>


    companion object {

        //private final const val KEY = "AIzaSyBaUPRMqZMOs8drD14sw25bCDD5QFHi6Cw"
        //private final const val KEY2 = "AIzaSyBVhdqkI4hsX2iJDyicTQxQqPrk7b4jYTk"
        //final const val KEY2 = "AIzaSyBVhdqkI4hsX2iJDyicTQxQqPrk7b4jYTk"
        //final const val KEY2 = "AIzaSyAYfqcFg2Vu9Nkrb-buFPy-zbqPbrmNoWE"
        //final const val KEY2 = "AIzaSyD-YVltn6eqtjPASNLNn8wqsBA_i16BLnA"
        final const val KEY2 = "AIzaSyDdGRDghuNkU8ewbsf8T_cvrS9fxe39_P4"

        //private const val KEY = "AIzaSyAYfqcFg2Vu9Nkrb-buFPy-zbqPbrmNoWE"
        private const val KEY = "AIzaSyDdGRDghuNkU8ewbsf8T_cvrS9fxe39_P4"
        //private const val KEY3 = "AIzaSyCjW8nV6QzOMQQt5PlYdzZlgTR63jB6dQU"
        //private const val KEY3 = "AIzaSyChDXUbavQGMp0QHKvTzwKd5re7kM4SQKA"
        //private const val KEY3 = "AIzaSyBhdimCg11eSsAieixZwVvJJKKCIIyFhE8"
        private const val KEY3 = "AIzaSyDdGRDghuNkU8ewbsf8T_cvrS9fxe39_P4"
        //private const val KEY2 = "AIzaSyBVhdqkI4hsX2iJDyicTQxQqPrk7b4jYTk"

        fun getRetrofit(): ApiServices {
            val retrofit: Retrofit =
                Retrofit.Builder().baseUrl("https://www.googleapis.com/youtube/v3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(ApiServices::class.java)
        }

    }
}