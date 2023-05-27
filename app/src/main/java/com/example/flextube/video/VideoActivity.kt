package com.example.flextube.video



import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.api.ApiServices
import com.example.flextube.comment.Comment
import com.example.flextube.comment.CommentAdapter
import com.example.flextube.comment.CommentApiModel
import com.example.flextube.databinding.ActivityVideoBinding
import com.example.flextube.interfaces.Formatter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    // comments variables
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<CommentAdapter.CommentViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var commentList: ArrayList<Comment> = ArrayList<Comment>()


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_video)
        setContentView(binding.root)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        val logo = binding.userLogo
        Picasso.get().load(account?.photoUrl.toString()).into(logo)

        mRecyclerView = binding.videoRecycleview
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        // getting object of video
        val intent = intent
        val gson = Gson()
        val json = intent.getStringExtra("video")
        val video: Video = gson.fromJson(json, Video::class.java)

        // sets a data of video object
        binding.itemVideoTitle.text = video.title
        binding.itemVideoInfo.text = "${video.viewCount} ${baseContext.resources.getString(R.string.views)} ∙ ${video.publishedDate}"
        // if likes are off then showing nothing
        if (video.likeCount != "-1")
            binding.itemVideoLike.text = video.likeCount
        binding.itemVideoChannelName.text = video.authorVideo.name

        if (video.authorVideo.subscriberCount != "-1")
            binding.itemVideoChannelSubs.text = "${video.authorVideo.subscriberCount} ${baseContext.resources.getString(
                R.string.channelSubs
            )}"
        Picasso.get().load(video.authorVideo.urlLogo).into(binding.itemVideoChannelLogo)
//        binding.itemVideoCommentCount.text = video.commentCount
        if (video.commentCount != "-1"){
            binding.itemVideoCommentCount.text = video.commentCount
            // getting comments by id of video
            getComments(video.id)
        } else {    // comments are off
            binding.noComments.visibility = View.VISIBLE
            binding.swipeBelow.visibility = View.GONE
            binding.itemVideoCommentCount.visibility = View.GONE
            binding.commentText.visibility = View.GONE
            binding.addCommentLayout.visibility = View.GONE
            binding.videoRecycleview.visibility = View.GONE
        }


        // webview settings
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



        // increase and decrease comment section (swipe and click version to choose)
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

        // addLike
        var isLikeAdded = false
        var isDisLikeAdded = false
        val like = binding.addLike
        like.setOnClickListener {
            if (isDisLikeAdded){
                binding.itemVideoDislikeImg.setImageResource(R.drawable.dislike_v2)
                isDisLikeAdded = !isDisLikeAdded
            }
            val likeImg = binding.itemVideoLikeImg
            isLikeAdded = if (isLikeAdded){

                likeImg.setImageResource(R.drawable.like_v2)
                !isLikeAdded
            }else {
                likeImg.setImageResource(R.drawable.ic_like_ful)
                !isLikeAdded
            }
        }
        // addDisLike

        binding.addDisLike.setOnClickListener {
            if (isLikeAdded){
                binding.itemVideoLikeImg.setImageResource(R.drawable.like_v2)
                isLikeAdded=!isLikeAdded
            }
            isDisLikeAdded = if (isDisLikeAdded){
                binding.itemVideoDislikeImg.setImageResource(R.drawable.dislike_v2)
                !isDisLikeAdded
            }else {
                binding.itemVideoDislikeImg.setImageResource(R.drawable.ic_dislike_full)
                !isDisLikeAdded
            }
        }

        // add subscribe
        var isSubAdded = false
        val sub = binding.addSubscribe
        sub.setOnClickListener {
//            val unwrappedDrawable = AppCompatResources.getDrawable(baseContext, R.drawable.background_subscriber)
//            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(applicationContext, R.color.greyly))
            isSubAdded = if (isSubAdded){
                sub.setBackgroundResource(R.drawable.background_subscriber)
                sub.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                sub.text = baseContext.resources.getString(R.string.sub)
                !isSubAdded
            }else {
                sub.setBackgroundResource(R.drawable.background_subscriber_added)
                sub.setTextColor(ContextCompat.getColor(applicationContext, R.color.blackly))
                sub.text = baseContext.resources.getString(R.string.subAdded)
                !isSubAdded
            }
        }

        binding.addComment.setOnClickListener {
            val comment = binding.commentContent.text
            if (comment.isNotEmpty()){
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                if (account != null) {
                    commentList.add(0,
                        Comment(
                            "qw321qwe321",
                            comment.toString(),
                            account.displayName.toString(),
            //                        "https://yt3.ggpht.com/JznZM_24hwJY-38C39RzvL4NjVBPQw9o9PKYz8fkgAGlDIel3R2Xij6Sdkt1HkaSClAFu3Vk=s240-c-k-c0x00ffffff-no-rj",
                            account.photoUrl.toString(),
                            "0",
                            currentDate+"T"
                        )
                    )
                }
                mRecyclerView.setHasFixedSize(true)
                mAdapter = CommentAdapter(commentList)
                mRecyclerView.layoutManager = mLayoutManager
                mRecyclerView.adapter = mAdapter
//                mAdapter.notifyDataSetChanged()
            }
        }
    }

    //getting comments by id of video
    private fun getComments(id: String) {
        Log.d("comment", "first")
        val retrofit = ApiServices.getRetrofit2()
        val comment: Call<CommentApiModel> = retrofit.getCommentsOfVideo(videoId = id)
        comment.enqueue(object : Callback<CommentApiModel> {
            override fun onResponse(
                call: Call<CommentApiModel>,
                response: Response<CommentApiModel>
            ) {
                Log.d("comment", "respone")
                Log.d("comment", response.code().toString())
                if (response.isSuccessful) {
                    Log.d("comment", "second")
                    val c = response.body()
                    for (i in c?.items!!) {
                        Log.d("comment", "third")
                        commentList.add(
                            Comment(
                                i.snippet.topLevelComment.id,
                                i.snippet.topLevelComment.snip.textDisplay,
                                i.snippet.topLevelComment.snip.authorDisplayName,
                                i.snippet.topLevelComment.snip.authorProfileImageUrl,
                                Formatter.formatNumber(i.snippet.topLevelComment.snip.likeCount.toString(),baseContext),
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
        Log.d("comment", "forth")
    }


}

