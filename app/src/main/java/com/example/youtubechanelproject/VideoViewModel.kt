package com.example.youtubechanelproject

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private var videoRepository: VideoRepository? = null
    var videosLiveData: LiveData<List<Video>>? = null
    private val channelIds = listOf("UCapJpINJKHzflWwCQ8Kse2g", "UCupvZG-5ko_eiXAupbDfxWw")

    init {
        /* val youTubeApi = RetrofitClient.getClient("https://www.googleapis.com/youtube/v3/")
             .create(YouTubeApiService::class.java)
         videoRepository = VideoRepository(youTubeApi)
         */
        //fahad
        //    videos = videoRepository.getLatestVideos("UCapJpINJKHzflWwCQ8Kse2g", "AIzaSyCNoGaDbAHCDx5-Hjqa3B1WFLZjfyOvTEA")
        //CNN
        //   videos = videoRepository.getLatestVideos("UCupvZG-5ko_eiXAupbDfxWw", "AIzaSyCNoGaDbAHCDx5-Hjqa3B1WFLZjfyOvTEA")

        //   videosLiveData = videoRepository.getLatestVideosFromChannels(channelIds,3,MainActivity.Google_Cloud_API_KEY )
    }

    fun getYoutubeVideosFromNetwork() {
        Log.e("getYoutubeVideosFromNetwork","inside__zz_called")
        val youTubeApi = RetrofitClient.getClient("https://www.googleapis.com/youtube/v3/")
            .create(YouTubeApiService::class.java)
        videoRepository = VideoRepository(youTubeApi)
        videosLiveData = videoRepository!!.getLatestVideosFromChannels(
            channelIds,
            3,
            MainActivity.Google_Cloud_API_KEY)
    }
}