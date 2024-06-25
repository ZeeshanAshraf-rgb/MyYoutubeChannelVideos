package com.example.youtubechanelproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
class VideoRepository(private val youTubeApi: YouTubeApi) {

    fun getLatestVideos(channelId: String, apiKey: String): LiveData<List<Video>> {
        val data = MutableLiveData<List<Video>>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val publishedAfter =
            dateFormat.format(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000))

        youTubeApi.getLatestVideos("snippet", channelId, publishedAfter, 10, apiKey)
            .enqueue(object : Callback<VideoResponse> {
                override fun onResponse(
                    call: Call<VideoResponse>,
                    response: Response<VideoResponse>
                ) {
                    if (response.isSuccessful) {
                        data.value = response.body()?.items
                    } else {
                        data.value = emptyList()
                    }
                }

                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    data.value = emptyList()
                }
            })

        return data
    }
}
*/


class VideoRepository(private val youTubeApi: YouTubeApiService ) {

    fun getLatestVideosFromChannels(channelIds: List<String>, maxResults: Int, apiKey: String): LiveData<List<Video>> {
        val videoDataList = MutableLiveData<List<Video>>()
        val allVideos = mutableListOf<Video>()


        var index = 0
        for (channelId in channelIds) {
            youTubeApi.getLatestVideos("snippet", channelId, maxResults, "date", apiKey)
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