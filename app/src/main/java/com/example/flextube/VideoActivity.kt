package com.example.flextube


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
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
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
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
        binding.itemVideoInfo.text = "${video.viewCount} ${baseContext.resources.getString(R.string.views)} ∙ ${video.publishedDate}"
        binding.itemVideoLike.text = video.likeCount
        binding.itemVideoChannelName.text = video.authorVideo.name
        binding.itemVideoChannelSubs.text = "${video.authorVideo.subscriberCount} ${baseContext.resources.getString(R.string.channelSubs)}"
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

        // increase and decrease comment section (swipe and click version)
        val changeBelowComment = binding.changeBelow

        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val btnBelow = binding.swipeBelow
//        btnBelow.setOnTouchListener(object : OnSwipeTouchListener(baseContext){
//            override fun onSwipeTop(){
//                params.addRule(RelativeLayout.BELOW, binding.videoDisplay.id)
//                changeBelowComment.layoutParams = params
//            }
//
//            override fun onSwipeBottom() {
//                super.onSwipeBottom()
//                params.addRule(RelativeLayout.BELOW, binding.toDownLayout.id)
//                changeBelowComment.layoutParams = params
//            }
//        })


        var belowedLayout = false
        btnBelow.setOnClickListener {
            if (belowedLayout)
                params.addRule(RelativeLayout.BELOW, binding.toDownLayout.id)
            else
                params.addRule(RelativeLayout.BELOW, binding.videoDisplay.id)
            changeBelowComment.layoutParams = params
            belowedLayout = !belowedLayout
        }

    }

    private fun getComments(id: String) {
        val retrofit = ApiServices.getRetrofit()
        val comment: Call<CommentApiModel> = retrofit.getCommentsOfVideo(videoId = id)
        comment.enqueue(object : Callback<CommentApiModel> {
            override fun onResponse(
                call: Call<CommentApiModel>,
                response: Response<CommentApiModel>
            ) {
                if (response.isSuccessful) {
                    val c = response.body()
                    for (i in c?.items!!) {
                        commentList.add(
                            Comment(
                                i.snippet.topLevelComment.id,
                                i.snippet.topLevelComment.snip.textDisplay,
                                i.snippet.topLevelComment.snip.authorDisplayName,
                                i.snippet.topLevelComment.snip.authorProfileImageUrl,
                                convertNumbers(i.snippet.topLevelComment.snip.likeCount.toString()),
                                i.snippet.topLevelComment.snip.publishedAt
                            )
                        )
                    }
                }
                mRecyclerView.setHasFixedSize(true)
                mAdapter = CommentAdapter(commentList)
                mRecyclerView.layoutManager = mLayoutManager
                mRecyclerView.adapter = mAdapter
//                mAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CommentApiModel>, t: Throwable) {
                Log.i("RETROFIT/COMMENT", "no works")
                Log.i("RETROFIT/COMMENT", t.message.toString())
            }
        })


    }
    private fun convertNumbers(num: String) : String{
        Log.i("LICZBA", num)
        var s = num.reversed()
        var new = ""
        var count = 0
        for (i in s){
            if (count == 3){
                new += "."
                count = 0
            }
            new += i
            count++
        }
        var indexStart = 0
        var amount = ""
        if (new.length >= 12){
            indexStart = 12
            amount = baseContext.resources.getString(R.string.num1000000000)
        }else if (new.length > 10){
            indexStart = 8
            amount = baseContext.resources.getString(R.string.num1000000)
        } else if (new.length >= 9){
            indexStart = 6
            amount = baseContext.resources.getString(R.string.num1000000)
        } else if (new.length > 4){
            indexStart = 4
            amount = baseContext.resources.getString(R.string.num1000)
        } else if (new.length == 4){
            indexStart = 2
            amount = baseContext.resources.getString(R.string.num1000)
        }
        s = new.subSequence(indexStart, new.length).toString()
        if ((indexStart == 4 || indexStart == 6) && s[0] == '0')
            s = s.subSequence(2,s.length).toString()
        Log.i("VIDEOCHECKNUMBER", s.reversed() + amount)
        return s.reversed() + amount
    }
}

