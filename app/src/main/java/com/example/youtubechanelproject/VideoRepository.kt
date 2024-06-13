package com.example.youtubechanelproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
