package com.example.flextube.api


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.flextube.auth.TokenResponse


import com.example.flextube.playlist.PlaylistApiModel


import com.example.flextube.comment.CommentApiModel
import com.example.flextube.playlist_item.PlaylistItemApiModel

import com.example.flextube.video.AuthorApiModel
import com.example.flextube.shorts.ShortsApiModel
import com.example.flextube.shorts.ShortsAuthorApiModel
import com.example.flextube.video.VideoApiModel
import com.example.flextube.video.VideoIdsApiModel
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {

    // returns image date and name of channel
    @GET("search")
    fun getSearchedVideos(
        @Query("part") part: String = "snippet",
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
    ) : Call<VideoApiModel>

    @GET("channels")
    fun getChannel(
        @Query("part") part: String = "snippet",
        @Query("part") part2: String = "statistics",
        @Query("id") id: String,
    ): Call<AuthorApiModel>

    @GET("commentThreads")
    fun getCommentsOfVideo(
        @Query("part") part: String ="snippet",
        @Query("order") order: String = "relevance",
        @Query("videoId") videoId: String,
        @Query("maxResults") results: Int = 40,
        @Query("textFormat") textFormat: String = "plainText",
        @Query("key") key: String = KEY
    ) : Call<CommentApiModel>

    @GET("playlists")
    fun getPlaylist(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("pageToken") pageToken: String?,
        @Query("maxResults") maxResults: Int
    ): Call<PlaylistApiModel>

    @GET("playlistItems")
    fun getPlaylistItems(
        @Query("part") part: String,
        @Query("playlistId") playlistId: String,
        @Query("key") key: String = KEY2,
        @Query("maxResults") maxResults: Int = 10
    ): Call<PlaylistItemApiModel>

    @GET("search")
    fun getShorts(
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("videoDuration") videoDuration: String = "short",
        @Query("maxResults") results: Int = 5,
    ): Call<VideoIdsApiModel>

    @GET("videos")
    fun getStatsShorts(
        @Query("part") part: String = "contentDetails",
        @Query("part") part2: String = "statistics",
        @Query("part") part3: String = "snippet",
        @Query("part") part4: String = "player",
        @Query("id") id: String,
    ): Call<ShortsApiModel>
    @GET("channels")
    fun getShortsChannel(
        @Query("part") part: String = "snippet",
        @Query("part") part2: String = "statistics",
        @Query("id") id: String,
    ): Call<ShortsAuthorApiModel>

//    @POST("https://accounts.google.com/o/oauth2/v2/auth")
//    @FormUrlEncoded
//    fun getAuthorization(
//        @Field("client_id") clientId: String="469398138855-2e433poad55hgnvjhe2l7ljj2b09bkqg.apps.googleusercontent.com",
//        @Field("redirect_uri") redirectUri: String="http://localhost:8080",
//        @Field("response_type") responseType: String="code",
//        @Field("scope") scope: String="https://www.googleapis.com/auth/youtube"
//    ) : Call<ResponseBody>

    @Headers("Accept: application/x-www-form-urlencoded")
    @POST("token")
    @FormUrlEncoded
    fun getToken(
        @Field("client_id") clientId: String = "469398138855-2l543p9gbvvfe1hnirm7m1b6au97v6g5.apps.googleusercontent.com",
        @Field("client_secret") clientSecret: String = "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",
        @Field("redirect_uri") redirectUri: String = "http://localhost:8080",
        @Field("code_challenge") codeVerifier: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): Call<TokenResponse>

    @POST("token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("client_id") clientId: String = "469398138855-2l543p9gbvvfe1hnirm7m1b6au97v6g5.apps.googleusercontent.com",
        @Field("client_secret") clientSecret: String = "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",
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String="refresh_token"
    ): Call<TokenResponse>


    companion object {
        //private final const val KEY = "AIzaSyChDXUbavQGMp0QHKvTzwKd5re7kM4SQKA"
        //private final const val KEY2 = "AIzaSyBVhdqkI4hsX2iJDyicTQxQqPrk7b4jYTk"
        //private final const val KEY3 = "AIzaSyChDXUbavQGMp0QHKvTzwKd5re7kM4SQKA"
        const val KEY2 = "AIzaSyChDXUbavQGMp0QHKvTzwKd5re7kM4SQKA"
        //const val KEY2 = "AIzaSyAd9PX3jGH7PmJoUJmFiFvV6NJ0pMfwQew"
        const val KEY = "AIzaSyAYfqcFg2Vu9Nkrb-buFPy-zbqPbrmNoWE"
        var authToken: String = ""


        fun getClient(): ApiServices {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()

            val retrofit: Retrofit = Retrofit.Builder()

                .baseUrl("https://oauth2.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
            return retrofit.create(ApiServices::class.java)
        }


        fun getRetrofit(): ApiServices {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request: Request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $authToken")
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
        fun getRetrofit2(): ApiServices {

            val retrofit: Retrofit =
                Retrofit.Builder().baseUrl("https://www.googleapis.com/youtube/v3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(ApiServices::class.java)

        }

    }
}