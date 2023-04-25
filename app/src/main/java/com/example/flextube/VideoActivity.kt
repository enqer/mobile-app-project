package com.example.flextube


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.flextube.databinding.ActivityVideoBinding
import com.example.flextube.video.Video
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_video)
        setContentView(binding.root)


        val intent = intent
        val gson = Gson()
        val json = intent.getStringExtra("video")
        val video: Video = gson.fromJson(json, Video::class.java)
        Log.i("video player", video.playerHtml)

        // view the date of video
        binding.itemVideoTitle.text = video.title
        binding.itemVideoInfo.text = "${video.viewCount} views ∙ ${video.publishedDate}"
        binding.itemVideoLike.text = video.likeCount + " likes"
        binding.itemVideoChannelName.text = video.authorVideo.name
        binding.itemVideoChannelSubs.text = video.authorVideo.subscriberCount + " subskrypcji"
        Picasso.get().load(video.authorVideo.urlLogo).into(binding.itemVideoChannelLogo)
        binding.itemVideoCommentCount.text = video.commentCount


        val webView: WebView = binding.videoDisplay
        webView.setInitialScale(1)
        webView.webChromeClient = WebChromeClient()
        webView.settings.allowFileAccess = true
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        val height = displaymetrics.heightPixels
        val width = displaymetrics.widthPixels

        val VIDEO_URL = "https://www.youtube.com/watch?v=IcqKOeK8ZcU"
        val data_html =
            "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> " +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> " +
                    "<link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" />" +
                    "</head> " +
                    "<body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> " + video.playerHtml+
//                    "<iframe style=\"background:black;\" width=' $width' height='$height' src=\"$VIDEO_URL\" frameborder=\"0\"></iframe> " +
                    "</body> </html> "

        webView.loadDataWithBaseURL("https://www.youtube.com", data_html, "text/html", "UTF-8", null);



    }
}