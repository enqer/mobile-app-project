package com.example.flextube.playlist_item

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.api.ApiServices
import com.example.flextube.video.AuthorApiModel
import com.example.flextube.video.AuthorVideo
import com.example.flextube.video.Video
import com.example.flextube.video.VideoActivity
import com.example.flextube.video.VideoApiModel

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class PlaylistActivity : AppCompatActivity() {
    private var playlistListId: String = ""
    private val playlistItemList: ArrayList<PlaylistItem> = ArrayList()
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<PlaylistItemAdapter.PlaylistItemViewHolder>

    //private var videoOwnerChannelId: String = ""

    private val channelList: ArrayList<AuthorVideo> = ArrayList()

    //private var videoId: String = ""
    public val videoItemList: ArrayList<Video> = ArrayList()
    public var authorList: ArrayList<AuthorVideo> = ArrayList<AuthorVideo>()
    var idAuthorsVideos: HashMap<String, String> = HashMap<String, String>()

    var iterator = 0

    private var authorId: String = ""
    private var authorName: String = ""
    private var authorUrlLogo: String = ""
    private var authorSubscriberCount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        Log.d(TAG, "PLaylistActivity/onCreate -> Start activity")

        val account = GoogleSignIn.getLastSignedInAccount(this)
        val playlistName = findViewById<TextView>(R.id.TV_playlist_name_activity)
        val logo = findViewById<ImageView>(R.id.person_icon)

        // set photo and person name
        Picasso.get().load(account.photoUrl.toString()).into(logo)

        val intent = intent
        playlistListId = intent.getStringExtra("id").toString()
        val title = intent.getStringExtra("title")


        // Inicjalizacja widoków i adaptera
        mRecyclerView = findViewById(R.id.playlist_items_recyclerview)
        mAdapter = PlaylistItemAdapter(
            playlistItemList,
            channelList,
            object : PlaylistItemAdapter.ItemClickListener {
                override fun onItemClick(playlistItem: PlaylistItem) {
                    // Obsługa kliknięcia na element listy
                    Log.d(TAG, "click click click")
                }

//            override fun onChannelItemClick(channelItem: AuthorVideo) {
//                // Obsługa kliknięcia na ikonkę kanału
//                Log.d(TAG, "click click click")
//            }
            })

        // Konfiguracja RecyclerView
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)


        Log.d(TAG, "PlaylistActivity/onCreate -> playlistListId: $playlistListId")
        Log.d(TAG, "PlaylistActivity/onCreate -> title: $title")

        playlistName.setText(title)

        getPlaylistItems()
    }

    private fun getPlaylistItems() {
        val maxLength = 22
        val apiServices = ApiServices.getRetrofit()
        apiServices.getPlaylistItems(
            "snippet, contentDetails",
            playlistListId,
            ApiServices.KEY2,
            10
        )
            .enqueue(object : Callback<PlaylistItemApiModel> {
                override fun onResponse(
                    call: Call<PlaylistItemApiModel>,
                    response: Response<PlaylistItemApiModel>
                ) {
                    if (response.isSuccessful) {
                        val playlistItems = response.body()?.items
                        playlistItems?.let {
                            for (item in playlistItems) {

                                val id = item.id
                                var title = item.snippet.title
                                var channelTitle = item.snippet.videoOwnerChannelTitle
                                val thumbnails = item.snippet.thumbnails.medium.url
                                val videoOwnerChannelId = item.snippet.videoOwnerChannelId
                                val videoId = item.snippet.resourceId.videoId

                                if (title.length > maxLength) {
                                    title = title.substring(0, maxLength) + "..."
                                }
                                if (channelTitle.length > maxLength) {
                                    channelTitle = channelTitle.substring(0, maxLength) + "..."
                                }

                                Log.d(TAG, "id: $id")
                                Log.d(TAG, "Title: $title")
                                Log.d(TAG, "channelTitle: $channelTitle")
                                Log.d(TAG, "thumbnails: $thumbnails")
                                Log.d(TAG, "videoOwnerChannelId: $videoOwnerChannelId")
                                Log.d(TAG, "videoId: $videoId")


                                getIconUser(videoOwnerChannelId)


                                playlistItemList.add(
                                    PlaylistItem(
                                        id,
                                        title,
                                        channelTitle,
                                        thumbnails,
                                        videoOwnerChannelId,
                                        videoId

                                    )
                                )
                            }

                            mAdapter = PlaylistItemAdapter(
                                playlistItemList,
                                channelList,
                                object : PlaylistItemAdapter.ItemClickListener {
                                    override fun onItemClick(playlistItem: PlaylistItem) {
                                        Log.d(TAG, "Item clicked: ${playlistItem.title}")
                                        Log.d(TAG, "Item clicked: ${playlistItem.videoId}")
                                        getIconUser(playlistItem.videoOwnerChannelId)
                                        Log.d(TAG, "Item getIconUser $authorId")
                                        Log.d(TAG, "Item getIconUser $authorName")
                                        Log.d(TAG, "Item getIconUser $authorUrlLogo")
                                        Log.d(TAG, "Item getIconUser $authorSubscriberCount")
                                        getVideoItems(playlistItem.videoId)
                                    }
                                }
                            )


                            mLayoutManager = LinearLayoutManager(this@PlaylistActivity)
                            mRecyclerView = findViewById(R.id.playlist_items_recyclerview)
                            mRecyclerView.layoutManager = mLayoutManager
                            mRecyclerView.adapter = mAdapter
                            mRecyclerView.setHasFixedSize(true)

                            mAdapter.notifyDataSetChanged() // Aktualizacja adaptera

                        }
                    } else {
                        Log.e(TAG, "Response not successful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PlaylistItemApiModel>, t: Throwable) {
                    Log.e(TAG, "Error fetching playlist: ${t.localizedMessage}")
                }
            })
    }

    private fun getIconUser(videoOwnerChannelId: String) {
        val apiServices = ApiServices.getRetrofit()
        apiServices.getChannel("snippet", "statistics", videoOwnerChannelId, ApiServices.KEY2)
            .enqueue(object : Callback<AuthorApiModel> {
                override fun onResponse(
                    call: Call<AuthorApiModel>,
                    response: Response<AuthorApiModel>
                ) {

                    if (response.isSuccessful) {
                        val channelItem = response.body()?.items
                        channelItem?.let {
                            for (item in channelItem) {
                                authorId = item.id
                                authorName = item.snippet.title
                                authorUrlLogo = item.snippet.thumbnails.picture.url
                                authorSubscriberCount = item.statistics.subscriberCount

                                Log.d(TAG, "id: $authorId")
                                Log.d(TAG, "name: $authorName")
                                Log.d(TAG, "urlLogo: $authorUrlLogo")
                                Log.d(TAG, "subscriberCount: $authorSubscriberCount")

                                channelList.add(
                                    AuthorVideo(
                                        authorId,
                                        authorName,
                                        authorUrlLogo,
                                        authorSubscriberCount
                                    )
                                )
                            }
                            mAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e(TAG, "Response not successful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<AuthorApiModel>, t: Throwable) {
                    Log.e(TAG, "Error fetching playlist: ${t.localizedMessage}")
                }

            })
    }

    private fun getVideoItems(videoId: String) {
        val apiServices = ApiServices.getRetrofit()
        apiServices.getStatsVideos(
            "contentDetails", "statistics", "snippet", "player",
            videoId, ApiServices.KEY2
        )
            .enqueue(object : Callback<VideoApiModel> {
                override fun onResponse(
                    call: Call<VideoApiModel>,
                    response: Response<VideoApiModel>
                ) {
                    Log.d(TAG, "videoId: $videoId")

                    if (response.isSuccessful) {
                        val videoItems = response.body()?.items
                        videoItems?.let {

                            for (item in videoItems) {


                                val id = item.id
                                val urlPhoto = item.snippet.thumbnails.photoVideo.urlPhoto
                                val duration = item.contentDetails.duration
                                val title = item.snippet.title
                                val viewCount = item.statistics.viewCount
                                val likeCount = item.statistics.likeCount
                                val commentCount = item.statistics.commentCount
                                val publishedAt = item.snippet.publishedAt
                                val embedHtml = item.player.embedHtml
                                val embedHeight = item.player.embedHeight
                                val embedWidth = item.player.embedWidth

                                Log.d(TAG, "id: $id")
                                Log.d(TAG, "urlPhoto: $urlPhoto")
                                Log.d(TAG, "duration: $duration")
                                Log.d(TAG, "title: $title")
                                Log.d(TAG, "viewCount: $viewCount")
                                Log.d(TAG, "likeCount: $likeCount")
                                Log.d(TAG, "commentCount: $commentCount")
                                Log.d(TAG, "publishedAt: $publishedAt")
                                Log.d(TAG, "embedHtml: $embedHtml")
                                Log.d(TAG, "embedHeight: $embedHeight")
                                Log.d(TAG, "embedWidth: $embedWidth")

                                Log.d(TAG, "authorId: $authorId")
                                Log.d(TAG, "authorName: $authorName")
                                Log.d(TAG, "authorUrlLogo: $authorUrlLogo")
                                Log.d(TAG, "authorSubscriberCount: $authorSubscriberCount")

                                videoItemList.add(
                                    Video(
                                        id,
                                        urlPhoto,
                                        duration,
                                        title,
                                        viewCount,
                                        likeCount,
                                        commentCount,
                                        publishedAt,
                                        embedHtml,
                                        embedHeight,
                                        embedWidth,
                                        AuthorVideo(
                                            authorId,
                                            authorName,
                                            authorUrlLogo,
                                            authorSubscriberCount
                                        )
                                    )
                                )
                            }

//                            val intent = Intent(applicationContext, VideoActivity::class.java)
//                            val gson = Gson()
//                            val json: String = gson.toJson(videoItemList)
//                            intent.putExtra("video", json)
//                            startActivity(intent)


                        }
                    } else {
                        Log.e(TAG, "Response not successful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<VideoApiModel>, t: Throwable) {
                    Log.e(TAG, "Error fetching playlist: ${t.localizedMessage}")
                }
            })
    }
}