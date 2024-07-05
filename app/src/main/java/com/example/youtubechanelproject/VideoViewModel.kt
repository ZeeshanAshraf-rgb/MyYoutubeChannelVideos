package com.example.youtubechanelproject

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private var videoRepository: VideoRepository? = null
    var videosLiveData: LiveData<List<Video>>? = null
    //private val channelIds = listOf("UCapJpINJKHzflWwCQ8Kse2g", "UCupvZG-5ko_eiXAupbDfxWw")
    private val channelIds = listOf( "UCaszgR2TH3qNw_CxLHAd2SQ",
        "UCazLt7y_UefRliODaUQznxg",
        "UCzMWRU-8D1IDafX2lA8d5xA",
        "UCxO4FDEsoD7geDkoIuOaCyw",
        "UCY7eFg5TEMsk_aTqM0pVB4w",
        "UCnkYymEbl1qZ0VhGrzH9guw",
        "UCeqdeoG3nRSkkbBKT9u3wQQ",
        "UCxaMbYDd4o_zVvfr9_wKAxQ",
        "UC620CWJ3QI2NjKFKhwKU2aw",
        "UCpkGqplkgxbHtzwHFPzzvUA",
        "UCAsvFcpUQegneSh0QAUd64A",
        "UCKD6btkq3JYZc7a6XpssMkA",
        "UCUszZwlbtq7R4ltY5pTRDww",
        "UC-gVcoup_u1yeGkqav-Lqkw",
        "UCCWpMUHRxsokns1bwrWvGCg",
        "UCFqEO3bSGcp5I8HVgXYdYRQ",
        "UCqGdM1DKE7iW2wH21QmwHoQ",
        "UC8jWGeUHBDg6Vbq1rICVwBA",
        "UCGhb5Gy-TAp0XWV26za_mcA",
        "UC1yuv89ftFyDQ8Ibre7cRgg",
        "UCVmuknBRlZvxDLJqnDfP7Ag")


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
            1,
            MainActivity.Google_Cloud_API_KEY)
    }
}