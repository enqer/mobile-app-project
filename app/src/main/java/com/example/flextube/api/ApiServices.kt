package com.example.flextube.api

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.flextube.auth.TokenResponse
import com.example.flextube.video.AuthorApiModel
import com.example.flextube.shorts.ShortsApiModel
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
import retrofit2.http.POST
import retrofit2.http.Query
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

interface ApiServices {

    // returns image date and name of channel
    @GET("search")
    fun getVideos(
        @Query("part") part: String = "snippet",
        @Query("maxResults") results: Int = 10  // 10 filmów się wyświetli na głównej tylko defaultowo jest 5 na api
    ): Call<VideoIdsApiModel>

    // return views depends on id
    @GET("videos")
    fun getStatsVideos(
        @Query("part") part: String = "contentDetails",
        @Query("part") part2: String = "statistics",
        @Query("part") part3: String = "snippet",
        @Query("id") id: String,
        //@Query("key") key: String = KEY3
    ): Call<VideoApiModel>

    @GET("channels")
    fun getChannel(
        @Query("part") part: String = "snippet",
        @Query("part") part2: String = "statistics",
        @Query("id") id: String,
        //@Query("key") key: String = KEY2
    ): Call<AuthorApiModel>

    @GET("search")
    fun getShorts(
        @Query("part") part: String = "snippet",
        //@Query("access_token") accessToken: String = "ya29.a0AWY7CklEBcM5A88zJklG4Iu_3PyfMUBt3UVBycQS5mERrwP1B2ygyGYlF2zu3ycJLBbB6tC103CtRq_RLxBtk2RdLoNoNlEukfLY48uXP-d9IzIb0F_fj6OZBREN9bTzKWAUYl7zeM48A9A90C3nR3-3P0O_aCgYKATQSARESFQG1tDrpcG0tbonRQNnt7qPXVy1wLQ0163",
        @Query("videoDuration") videoDuration: String = "short",
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: String = "17"
    ): Call<VideoIdsApiModel>

    @GET("videos")
    fun getStatsShorts(
        @Query("part") part: String = "contentDetails",
        @Query("part") part2: String = "statistics",
        @Query("part") part3: String = "snippet",
        @Query("part") part4: String = "player",
        @Query("id") id: String,
        // @Query("key") key: String = KEY
    ): Call<ShortsApiModel>
//    @POST("https://accounts.google.com/o/oauth2/v2/auth")
//    @FormUrlEncoded
//    fun getAuthorization(
//        @Field("client_id") clientId: String="469398138855-2e433poad55hgnvjhe2l7ljj2b09bkqg.apps.googleusercontent.com",
//        @Field("redirect_uri") redirectUri: String="http://localhost:8080",
//        @Field("response_type") responseType: String="code",
//        @Field("scope") scope: String="https://www.googleapis.com/auth/youtube"
//    ) : Call<ResponseBody>


    @POST("token")
    @FormUrlEncoded
    fun getToken(
        @Field("code") code: String,
        @Field("client_id") clientId: String = "469398138855-2qgn9emqks2dv1ou3mfcoo1upenj854e.apps.googleusercontent.com",
        @Field("client_secret") clientSecret: String = "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",
        @Field("redirect_uri") redirectUri: String = "http://127.0.0.1:9004",
        @Field("code_verifier") codeVerifier: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): Call<TokenResponse>


    companion object {
        private final const val KEY = "AIzaSyDrvd66oJPdfjmm8c93lOSupl0ls71uDB8"
        private final const val KEY2 = "AIzaSyBVhdqkI4hsX2iJDyicTQxQqPrk7b4jYTk"
        private final const val KEY3 = "AIzaSyAYfqcFg2Vu9Nkrb-buFPy-zbqPbrmNoWE"
        var authToken: String = ""


        fun getClient(): ApiServices {
            val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
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
            Log.i("tok", authToken)
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
            Log.i("ret", authToken)
            return retrofit.create(ApiServices::class.java)
        }
        fun getRetrofit2(): ApiServices {
            val retrofit: Retrofit =
                Retrofit.Builder().baseUrl("https://oauth2.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(ApiServices::class.java)

        }

    }


}