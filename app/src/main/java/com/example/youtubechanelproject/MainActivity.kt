package com.example.youtubechanelproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var videoViewModel: VideoViewModel
  //  private lateinit var videoAdapter: VideoAdapter
    var webView: WebView? = null
    var nextBtn: Button? = null

    private var videoIds = ArrayList<String>() // Add your YouTube video IDs here
    private var currentVideoIndex = 0
    private var selectedVideoId: String = ""

    companion object {
        const val Google_Cloud_API_KEY: String = "AIzaSyCNoGaDbAHCDx5-Hjqa3B1WFLZjfyOvTEA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_main)
        StorageSDK.init(this)
        webView = findViewById<WebView>(R.id.webview)
        nextBtn = findViewById<Button>(R.id.nextBtn)
       /* videoAdapter = VideoAdapter()
        videoAdapter.setListener(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = videoAdapter*/

        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        //videoViewModel.getYoutubeVideosFromNetwork()
        checkDataSavedBefore24Hours()
        videoViewModel.videosLiveData?.observe(this, Observer { videosDataModel ->
            Log.e("videosLiveData_observe", "inside_=" + videosDataModel?.size)
         //   videosDataModel?.let { videoAdapter.setVideos(it) }

            videosDataModel?.forEach {
                videoIds.add(it.id.videoId)
            }
            if (videosDataModel != null) {
                StorageSDK.saveVideosData(videosDataModel)
                currentVideoIndex = 0
                loadYouTubePlayer(videoIds[currentVideoIndex])
            }
        })


        val webSettings = webView?.getSettings()
        webSettings?.javaScriptEnabled = true;
        webSettings?.mediaPlaybackRequiresUserGesture =
            false; // Allow media playback without user gesture
        webView?.setWebViewClient(WebViewClient())
        webView?.addJavascriptInterface(this, "Android")


        nextBtn?.setOnClickListener{
            moveNext()
        }
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

        if (System.currentTimeMillis() - savedTimestamp >= 8 * 60 * 60 * 1000) {
            //it means more then 8 hours passed so sync data from api
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
                currentVideoIndex = 0
                loadYouTubePlayer(videoIds[currentVideoIndex])
          //      videoAdapter.setVideos(videosDataArrList)
            }
        }
    }

    private fun moveNext() {
        currentVideoIndex = (currentVideoIndex + 1) % videoIds.size
        loadYouTubePlayer(videoIds[currentVideoIndex])
    }

    private fun loadYouTubePlayer(videoId: String) {
        val html = ("<html><body>" +
                "<div id=\"player\"></div>" +
                "<script>" +
                "var tag = document.createElement('script');" +
                "tag.src = \"https://www.youtube.com/iframe_api\";" +
                "var firstScriptTag = document.getElementsByTagName('script')[0];" +
                "firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);" +
                "var player;" +
                "function onYouTubeIframeAPIReady() {" +
                "   player = new YT.Player('player', {" +
                "       height: '100%'," +
                "       width: '100%'," +
                "       videoId: '" + videoId).toString() + "'," +
                "       playerVars: {" +
                "           'autoplay': 1," +
                "           'controls': 1," +
                "           'loop': 1," +
                "       }," +
                "       events: {" +
                "           'onReady': onPlayerReady," +
                "           'onStateChange': onPlayerStateChange" +
                "       }" +
                "   });" +
                "}" +
                "function onPlayerReady(event) {" +
                "   event.target.setVolume(100);" +
                "   event.target.playVideo();" +
                "}" +
                "function onPlayerStateChange(event) {" +
                "   if (event.data == YT.PlayerState.ENDED) {" +
                "       Android.onVideoEnded();" +
                "   }" +
                "}" +
                "</script>" +
                "</body></html>"

        webView!!.loadData(html, "text/html", "utf-8")
    }

    @JavascriptInterface
    fun onVideoEnded() {
        Log.e("onVideoEnded", "inside_zzzzzzzzzz")
        runOnUiThread {
            currentVideoIndex = (currentVideoIndex + 1) % videoIds.size
            loadYouTubePlayer(videoIds.get(currentVideoIndex))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView = null
    }
}