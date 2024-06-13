package com.example.youtubechanelproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {
    @GET("search")
    fun getLatestVideos(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("publishedAfter") publishedAfter: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String
    ): Call<VideoResponse>
}
