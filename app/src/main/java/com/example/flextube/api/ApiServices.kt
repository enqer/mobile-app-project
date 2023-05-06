package com.example.flextube.api

import com.example.flextube.video.AuthorApiModel
import com.example.flextube.shorts.ShortsApiModel
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
        @Query("key") key: String = KEY2,
        @Query("maxResults") results: Int = 10  // 10 filmów się wyświetli na głównej tylko defaultowo jest 5 na api
    ) : Call<VideoIdsApiModel>

    // return views depends on id
    @GET("videos")
    fun getStatsVideos(
        @Query("part") part: String = "contentDetails",
        @Query("part") part2: String = "statistics",
        @Query("part") part3: String = "snippet",
        @Query("id") id: String,
        @Query("key") key: String = KEY2
    ) : Call<VideoApiModel>

    @GET("channels")
    fun getChannel(
        @Query("part") part: String = "snippet",
        @Query("part") part2: String = "statistics",
        @Query("id") id: String,
        @Query("key") key: String = KEY2
    ) : Call<AuthorApiModel>

    @GET("search")
    fun getShorts(
        @Query("part") part: String = "snippet",
        @Query("key") key: String = KEY2,
        @Query("videoDuration") videoDuration: String = "short",
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: String = "17"
    ) : Call<VideoIdsApiModel>

    @GET("videos")
    fun getStatsShorts(
        @Query("part") part: String = "contentDetails",
        @Query("part") part2: String = "statistics",
        @Query("part") part3: String = "snippet",
        @Query("part") part4: String = "player",
        @Query("id") id: String,
        @Query("key") key: String = KEY2
    ) : Call<ShortsApiModel>

    @GET("auth")
    fun auth(
        @Query("client_id") client_id: String = "460223798693-7pc3ftjusn904pa7b1faei2ci0r9ko45.apps.googleusercontent.com",
        @Query("redirect_uri") redirect_uri: String = "com.example.flextube",
        @Query("response_type") response_type: String = "code",
        @Query("scope") scope: String = "email%20profile",
        @Query("login_hint") login_hint: String
    ) : Call<String>


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
        fun getRetrofitAuth(): ApiServices {
            val retrofit: Retrofit =
                Retrofit.Builder().baseUrl("https://accounts.google.com/o/oauth2/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(ApiServices::class.java)
        }
    }
}