package com.example.youtubechanelproject.activity

import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.youtubechanelproject.R
import com.example.youtubechanelproject.others.StorageSDK
import com.example.youtubechanelproject.VideoViewModel
import com.example.youtubechanelproject.api.Video

class MainActivity : AppCompatActivity() {
    private lateinit var videoViewModel: VideoViewModel

    //  private lateinit var videoAdapter: VideoAdapter
    var webView: WebView? = null
    var nextBtn: Button? = null
    var prevBtn: Button? = null

    private var videosArrList = ArrayList<Video>() // Add your YouTube video IDs here
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
        prevBtn = findViewById<Button>(R.id.prevBtn)
        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        checkDataSavedBefore24Hours()
        videoViewModel.videosLiveData?.observe(this, Observer { videosDataList ->
            Log.e("videosLiveData_observe", "inside_=" + videosDataList?.size)
            videosDataList?.forEach {
                it.isWatched = false
                videosArrList.add(it)
            }
            if (videosDataList != null) {
                StorageSDK.saveVideosData(videosDataList)
                currentVideoIndex = -1
                playNextVideo()
               /* currentVideoIndex = 0
                loadYouTubePlayer(videosArrList[currentVideoIndex].id.videoId)*/
            }
        })


        val webSettings = webView?.getSettings()
        webSettings?.javaScriptEnabled = true;
        webSettings?.mediaPlaybackRequiresUserGesture =
            false; // Allow media playback without user gesture
        webView?.setWebViewClient(WebViewClient())
        webView?.addJavascriptInterface(this, "Android")


        nextBtn?.setOnClickListener {
            playNextVideo()
        }
        prevBtn?.setOnClickListener {
            playPreviousVideo()
        }

    }

  /*  override fun onVideoCLick(videoId: String) {
        Log.e("onVideoCLick", "videoId=>" + videoId)
        val intent = Intent(this, YoutubePlayerUsingWebViewActivity::class.java)
        intent.putStringArrayListExtra("videoIds_arrayList", videoIdsArrList)
        intent.putExtra("selected_videoId", videoId)
        startActivity(intent)
    }*/

    private fun checkDataSavedBefore24Hours() {
        var savedTimestamp = StorageSDK.getTimeStamp()

        if (System.currentTimeMillis() - savedTimestamp >= 8 * 60 * 60 * 1000) {
            //it means more then 8 hours passed so sync data from api
            StorageSDK.saveTimeStamp()
            videoViewModel.getYoutubeVideosFromNetwork()
        } else {
            Log.e("getYoutubeVideosFromNetwork", "inside__zz_not_called")

            videosArrList = StorageSDK.getVideosData()
            if (videosArrList.isNullOrEmpty()) {
                StorageSDK.saveTimeStamp()
                videoViewModel.getYoutubeVideosFromNetwork()
            } else {
              /*  videosDataArrList.forEach {
                    it.isWatched = false
                    videosArrList.add(it)
                }*/
                currentVideoIndex = -1
                playNextVideo()
               // loadYouTubePlayer(videosArrList[currentVideoIndex].id.videoId)
                //      videoAdapter.setVideos(videosDataArrList)
            }
        }
    }

    private fun playNextVideo() {
        currentVideoIndex += 1
        if (currentVideoIndex < videosArrList.size) {
            checkNextVideo(currentVideoIndex)
        } else {
            currentVideoIndex--
            Toast.makeText(this@MainActivity, "No, Next Videos are Available.", Toast.LENGTH_LONG).show()
        }
    }
    private fun checkNextVideo(selectedIndex: Int) {
        var flag = false
        for (item in selectedIndex until videosArrList.size) {
            if (!videosArrList[item].isWatched) {
                flag = true
                currentVideoIndex = item
                loadYouTubePlayer(videosArrList[currentVideoIndex].id.videoId)
                break
            }
        }
        if (!flag) {
            Toast.makeText(this@MainActivity, "No, Next Videos are Available.", Toast.LENGTH_LONG).show()
        }
    }
    private fun checkPrevVideo(prevIndex: Int) {
        var flag = false

        for (itemIndex in prevIndex downTo 0) {
            if (!videosArrList[itemIndex].isWatched) {
                flag = true
                currentVideoIndex = itemIndex
                loadYouTubePlayer(videosArrList[currentVideoIndex].id.videoId)
                break
            }
        }
        if (!flag) {
            Toast.makeText(this@MainActivity, "No, Previous Videos are Available.", Toast.LENGTH_LONG).show()
        }
    }
    private fun playPreviousVideo() {
        if(currentVideoIndex <= 0){
            Toast.makeText(this@MainActivity, "No, Previous Videos are Available.", Toast.LENGTH_LONG).show()

        } else {
            currentVideoIndex -= 1
            if (currentVideoIndex >= 0) {
                checkPrevVideo(currentVideoIndex)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "No, Previous Videos are Available.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
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
        Log.e("onVideoEnded", "inside_currentVI="+currentVideoIndex)
        runOnUiThread {
            videosArrList[currentVideoIndex].isWatched = true
            StorageSDK.saveVideosData(videosArrList.toList())
            //save in storage
           // currentVideoIndex = (currentVideoIndex + 1) % videosArrList.size
            playNextVideo()
           // loadYouTubePlayer(videosArrList[currentVideoIndex].id.videoId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView = null
    }
}