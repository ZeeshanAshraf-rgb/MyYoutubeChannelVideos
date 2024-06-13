package com.example.youtubechanelproject


import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class YoutubeMainActivity : AppCompatActivity() {
    var webView: WebView? = null

    private var videoIds = ArrayList<String>() // Add your YouTube video IDs here
    private var currentVideoIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_youtube_main)

        videoIds = intent.getStringArrayListExtra("videoIds_arrayList")!!
        webView = findViewById<WebView>(R.id.webview)


        val webSettings = webView?.getSettings()
        webSettings?.javaScriptEnabled = true
        webSettings?.loadWithOverviewMode = true
        webSettings?.useWideViewPort = true

        webView?.setWebViewClient(WebViewClient())
        webView?.addJavascriptInterface(this, "Android")

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
                "           'controls': 1" +
                "       }," +
                "       events: {" +
                "           'onReady': onPlayerReady," +
                "           'onStateChange': onPlayerStateChange" +
                "       }" +
                "   });" +
                "}" +
                "function onPlayerReady(event) {" +
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
    // This method will be called from JavaScript when a video ends
    @JavascriptInterface
    fun onVideoEnded() {
        Log.e("onVideoEnded","inside_zzzzzzzzzz")
        runOnUiThread {
            currentVideoIndex = (currentVideoIndex + 1) % videoIds.size
            loadYouTubePlayer(videoIds.get(currentVideoIndex))
        }
    }

}