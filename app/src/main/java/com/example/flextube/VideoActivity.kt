package com.example.flextube


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.api.ApiServices
import com.example.flextube.comment.Comment
import com.example.flextube.comment.CommentAdapter
import com.example.flextube.comment.CommentApiModel
import com.example.flextube.databinding.ActivityVideoBinding
import com.example.flextube.video.Video
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<CommentAdapter.CommentViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var commentList: ArrayList<Comment> = ArrayList<Comment>()

//    val webView = binding.videoDisplay

    @RequiresApi(Build.VERSION_CODES.O)
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
        binding.itemVideoLike.text = video.likeCount
        binding.itemVideoChannelName.text = video.authorVideo.name
        binding.itemVideoChannelSubs.text = video.authorVideo.subscriberCount + " subskrypcji"
        Picasso.get().load(video.authorVideo.urlLogo).into(binding.itemVideoChannelLogo)
        binding.itemVideoCommentCount.text = video.commentCount

        val useragent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"

        val webView: WebView = binding.videoDisplay
        webView.setInitialScale(1)
        webView.webChromeClient = WebChromeClient()
        webView.settings.allowFileAccess = true
        webView.settings.mediaPlaybackRequiresUserGesture=false
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.userAgentString = useragent

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
//        webView.loadUrl("$data_html?autoplay=1")
        webView.loadDataWithBaseURL("https://www.youtube.com", data_html, "text/html", "UTF-8", null);

        mRecyclerView = binding.videoRecycleview
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        getComments(video.id)

    }

    private fun getComments(id: String){
        val retrofit = ApiServices.getRetrofit()
        val comment: Call<CommentApiModel> = retrofit.getCommentsOfVideo(videoId = id)
        comment.enqueue(object : Callback<CommentApiModel> {
            override fun onResponse(
                call: Call<CommentApiModel>,
                response: Response<CommentApiModel>
            ) {
                if (response.isSuccessful){
                    val c = response.body()
                    for (i in c?.items!!){
                        commentList.add(
                            Comment(
                            i.snippet.topLevelComment.id,
                            i.snippet.topLevelComment.snip.textDisplay,
                            i.snippet.topLevelComment.snip.authorDisplayName,
                            i.snippet.topLevelComment.snip.authorProfileImageUrl,
                            i.snippet.topLevelComment.snip.likeCount,
                            i.snippet.topLevelComment.snip.publishedAt
                        )
                        )
                    }
                }
                mRecyclerView.setHasFixedSize(true)
                mAdapter = CommentAdapter(commentList)
                mRecyclerView.layoutManager=mLayoutManager
                mRecyclerView.adapter = mAdapter
//                mAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CommentApiModel>, t: Throwable) {
                Log.i("RETROFIT/COMMENT", "no works")
                Log.i("RETROFIT/COMMENT", t.message.toString())
            }
        })
    }


}