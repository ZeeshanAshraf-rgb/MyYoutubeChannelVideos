package com.example.youtubechanelproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class VideoRepository(private val youTubeApi: YouTubeApiService ) {

    fun getLatestVideosFromChannels(channelIds: List<String>, maxResults: Int, apiKey: String): LiveData<List<Video>> {
        val videoDataList = MutableLiveData<List<Video>>()
        val allVideos = mutableListOf<Video>()
// Define the time range for the last 24 hours
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val nowIso = dateFormat.format(now)
        val yesterday = Date(now.time - 24 * 60 * 60 * 1000)
        val yesterdayIso = dateFormat.format(yesterday)


        var index = 0
        for (channelId in channelIds) {
            youTubeApi.getLatestVideosNew("snippet", channelId, maxResults, "date", apiKey,yesterdayIso,nowIso)
                .enqueue(object : Callback<YouTubeResponse> {
                    override fun onResponse(call: Call<YouTubeResponse>, response: Response<YouTubeResponse>) {
                        if (response.isSuccessful) {
                            index++
                            response.body()?.items?.let { videos ->
                                allVideos.addAll(videos)
                            }

                            if(index == channelIds.size) {
                                videoDataList.value = allVideos
                            }
                        }
                    }

                    override fun onFailure(call: Call<YouTubeResponse>, t: Throwable) {
                        videoDataList.value = null
                        Log.e("Error","onFailure="+t.toString())
                    }
                })
        }

        return videoDataList
    }
}