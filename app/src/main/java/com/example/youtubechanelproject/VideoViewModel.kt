package com.example.youtubechanelproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private val videoRepository: VideoRepository
    val videos: LiveData<List<Video>>

    init {
        val youTubeApi = RetrofitClient.getClient("https://www.googleapis.com/youtube/v3/")
            .create(YouTubeApi::class.java)
        videoRepository = VideoRepository(youTubeApi)
        //videos = videoRepository.getLatestVideos(YOUR_CHANNEL_ID, YOUR_API_KEY)
    //fahad
    //    videos = videoRepository.getLatestVideos("UCapJpINJKHzflWwCQ8Kse2g", "AIzaSyCNoGaDbAHCDx5-Hjqa3B1WFLZjfyOvTEA")
       //CNN
     //   videos = videoRepository.getLatestVideos("UCupvZG-5ko_eiXAupbDfxWw", "AIzaSyCNoGaDbAHCDx5-Hjqa3B1WFLZjfyOvTEA")
        videos = videoRepository.getLatestVideos("UCupvZG-5ko_eiXAupbDfxWw",MainActivity.Google_Cloud_API_KEY )
    }
}