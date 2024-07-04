package com.example.youtubechanelproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var videoViewModel: VideoViewModel
    private lateinit var videoAdapter: VideoAdapter

    companion object {
        const val Google_Cloud_API_KEY: String = "AIzaSyCNoGaDbAHCDx5-Hjqa3B1WFLZjfyOvTEA"
    }

    private var videoIds = ArrayList<String>() // Add your YouTube video IDs here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StorageSDK.init(this)
        videoAdapter = VideoAdapter()
        videoAdapter.setListener(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = videoAdapter

        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        //videoViewModel.getYoutubeVideosFromNetwork()
        checkDataSavedBefore24Hours()
        videoViewModel.videosLiveData?.observe(this, Observer { videosDataModel ->
            Log.e("videosLiveData_observe", "inside_=" + videosDataModel?.size)
            videosDataModel?.let { videoAdapter.setVideos(it) }

            videosDataModel?.forEach {
                videoIds.add(it.id.videoId)
            }
            if (videosDataModel != null)
                StorageSDK.saveVideosData(videosDataModel)
        })
    }

    override fun onVideoCLick(videoId: String) {
        Log.e("onVideoCLick", "videoId=>" + videoId)
        val intent = Intent(this, YoutubePlayerUsingWebViewActivity::class.java)
        intent.putStringArrayListExtra("videoIds_arrayList", videoIds)
        intent.putExtra("selected_videoId", videoId)
        startActivity(intent)
    }

    private fun checkDataSavedBefore24Hours() {
        var savedTimestamp = StorageSDK.getTimeStamp()

        if (System.currentTimeMillis() - savedTimestamp >= 6 * 60 * 60 * 1000) {
            //it means more then 6 hours passed so sync data from api
            StorageSDK.saveTimeStamp()
            videoViewModel.getYoutubeVideosFromNetwork()
        } else {
            Log.e("getYoutubeVideosFromNetwork", "inside__zz_not_called")

            var videosDataArrList = StorageSDK.getVideosData()
            if (videosDataArrList.isNullOrEmpty()) {
                StorageSDK.saveTimeStamp()
                videoViewModel.getYoutubeVideosFromNetwork()
            } else {
                videosDataArrList.forEach {
                    videoIds.add(it.id.videoId)
                }
                videoAdapter.setVideos(videosDataArrList)
            }
        }
    }
}