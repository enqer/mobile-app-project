package com.example.flextube.api

import com.example.flextube.video.AuthorApiModel
import com.example.flextube.shorts.ShortsApiModel
import com.example.flextube.video.VideoApiModel
import com.example.flextube.video.VideoIdsApiModel
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiServices {
    @Headers("Authorization: Bearer {TOKEN}")
    // returns image date and name of channel
    @GET("search")
    fun getVideos(
        @Query("part") part: String = "snippet",
        @Query("key") key: String = KEY3,
        @Query("maxResults") results: Int = 10  // 10 filmów się wyświetli na głównej tylko defaultowo jest 5 na api
    ) : Call<VideoIdsApiModel>

    // return views depends on id
    @GET("videos")
    fun getStatsVideos(
        @Query("part") part: String = "contentDetails",
        @Query("part") part2: String = "statistics",
        @Query("part") part3: String = "snippet",
        @Query("id") id: String,
        @Query("key") key: String = KEY3
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
        @Query("key") key: String = KEY3,
        @Query("videoDuration") videoDuration: String = "short",
        @Query("q") q: String = "shorts",
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
        @Query("key") key: String = KEY3
    ) : Call<ShortsApiModel>

    companion object {
        private final const val KEY = "AIzaSyBaUPRMqZMOs8drD14sw25bCDD5QFHi6Cw"
        private final const val KEY2 = "AIzaSyBVhdqkI4hsX2iJDyicTQxQqPrk7b4jYTk"
        private final const val KEY3= "AIzaSyAYfqcFg2Vu9Nkrb-buFPy-zbqPbrmNoWE"
        var authToken: String = ""



        fun getRetrofit(): ApiServices {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request: Request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $authToken") // dodaj nagłówek z tokenem autoryzacji
                        .build()
                    chain.proceed(request)
                }
                .build()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiServices::class.java)
        }
    }
}