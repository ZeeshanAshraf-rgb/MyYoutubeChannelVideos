package com.example.youtubechanelproject.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("search")
    fun getLatestVideos(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("publishedAfter") publishedAfter: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String
    ): Call<YouTubeResponse>

    @GET("search")
    fun getLatestVideos(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: Int,
        @Query("order") order: String,
        @Query("key") apiKey: String
    ): Call<YouTubeResponse>

    @GET("search")
    fun getLatestVideosNew(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: Int,
        @Query("order") order: String,
        @Query("key") apiKey: String,
        @Query("publishedAfter") publishedAfterKey: String,
        @Query("publishedBefore") publishedBeforeKey: String
    ): Call<YouTubeResponse>
}
