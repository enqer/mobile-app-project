package com.example.flextube.video

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
//    part={part}&key={key}
    @GET("search?part=snippet&key=AIzaSyBaUPRMqZMOs8drD14sw25bCDD5QFHi6Cw")
    fun getVideos(
//        @Query("part") part: String,
//        @Query("key") key: String
//        @Path("part") part: String,
//        @Path("key") key: String
    ) : Call<VideoApiModel>

}