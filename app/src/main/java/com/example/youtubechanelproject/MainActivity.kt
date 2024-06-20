package com.example.youtubechanelproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() , ItemClickListener{
    private lateinit var videoViewModel: VideoViewModel
    private lateinit var videoAdapter: VideoAdapter

    companion object {
        const val Google_Cloud_API_KEY: String = "AIzaSyCNoGaDbAHCDx5-Hjqa3B1WFLZjfyOvTEA"
    }
    private val videoIds = ArrayList<String>() // Add your YouTube video IDs here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoAdapter = VideoAdapter()
        videoAdapter.setListener(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = videoAdapter

        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        videoViewModel.getYoutubeVideosFromNetwork()

        videoViewModel.videosLiveData?.observe(this, Observer { videos ->
            Log.e("videosLiveData_observe","inside_="+videos?.size)
            videos?.let { videoAdapter.setVideos(it) }

            videos?.forEach {
                videoIds.add(it.id.videoId)
            }
        })

    }

    override fun onVideoCLick(videoId: String) {
        Log.e("onVideoCLick","videoId=>"+videoId)
        val intent = Intent(this, YoutubePlayerActivity::class.java)
        intent.putStringArrayListExtra("videoIds_arrayList",videoIds)
        intent.putExtra("selected_videoId",videoId)
        startActivity(intent)
    }
}