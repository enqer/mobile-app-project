package com.example.flextube.playlist_item

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
import com.example.flextube.database.DatabaseHelper
import com.example.flextube.settings.SettingsActivity
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

    private val channelList: ArrayList<AuthorVideo> = ArrayList()

    private var authorId: String = ""
    private var authorName: String = ""
    private var authorUrlLogo: String = ""
    private var authorSubscriberCount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        Log.d("PLaylistActivity/onCreate", "Start activity")

        val account = GoogleSignIn.getLastSignedInAccount(this)
        val playlistName = findViewById<TextView>(R.id.TV_playlist_name_activity)
        val logo = findViewById<ImageView>(R.id.person_icon)

        // set photo and person name
        Picasso.get().load(account.photoUrl.toString()).into(logo)

        val intent = intent
        playlistListId = intent.getStringExtra("id").toString()
        val title = intent.getStringExtra("title")

        val imageView = findViewById<ImageView>(R.id.person_icon)
        imageView.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        mRecyclerView = findViewById(R.id.playlist_items_recyclerview)
        mAdapter = PlaylistItemAdapter(
            playlistItemList,
            channelList,
            object : PlaylistItemAdapter.ItemClickListener {
                override fun onItemClick(playlistItem: PlaylistItem) {
                    Log.d("PlaylistActivity/onCreate/onItemClick", "click")
                }
            })

        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)

        Log.d("PlaylistActivity/onCreate", "playlistListId: $playlistListId")
        Log.d("PlaylistActivity/onCreate", "title: $title")

        playlistName.setText(title)

        getPlaylistItems()
    }

    private fun getPlaylistItems() {
        val maxLength = 22
        val apiServices = ApiServices.getRetrofit()
        apiServices.getPlaylistItems(
            "snippet, contentDetails",
            playlistListId,
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
                                var title = item.snippet.title
                                var channelTitle = item.snippet.videoOwnerChannelTitle

                                if (title.length > maxLength) {
                                    title = title.substring(0, maxLength) + "..."
                                }
                                if (channelTitle.length > maxLength) {
                                    channelTitle = channelTitle.substring(0, maxLength) + "..."
                                }

                                playlistItemList.add(
                                    PlaylistItem(
                                        item.id,
                                        title,
                                        channelTitle,
                                        item.snippet.thumbnails.medium.url,
                                        item.snippet.videoOwnerChannelId,
                                        item.snippet.resourceId.videoId
                                    )
                                )
                                getIconUser(item.snippet.videoOwnerChannelId)
                            }

                            mAdapter = PlaylistItemAdapter(
                                playlistItemList,
                                channelList,
                                object : PlaylistItemAdapter.ItemClickListener {
                                    override fun onItemClick(playlistItem: PlaylistItem) {
                                        Log.d("PlaylistActivity/PlaylistItems", "onItemClick click")
                                        getIconUser(playlistItem.videoOwnerChannelId)
                                        getIconUser(playlistItem.videoOwnerChannelId)
                                        getIconUser(playlistItem.videoOwnerChannelId)
                                        getVideoItems(playlistItem.videoId)
                                    }
                                }
                            )
                            mLayoutManager = LinearLayoutManager(this@PlaylistActivity)
                            mRecyclerView = findViewById(R.id.playlist_items_recyclerview)
                            mRecyclerView.layoutManager = mLayoutManager
                            mRecyclerView.adapter = mAdapter
                            mRecyclerView.setHasFixedSize(true)

                            mAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e(
                            "PlaylistActivity/PlaylistItems",
                            "Response not successful: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(call: Call<PlaylistItemApiModel>, t: Throwable) {
                    Log.e(
                        "PlaylistActivity/PlaylistItems/onFailure",
                        "Error fetching playlist: ${t.localizedMessage}"
                    )
                }
            })
    }

    private fun getIconUser(videoOwnerChannelId: String) {
        val apiServices = ApiServices.getRetrofit()
        apiServices.getChannel("snippet", "statistics", videoOwnerChannelId)
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
                        Log.e(
                            "PlaylistActivity/getIconUser",
                            "Response not successful: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(call: Call<AuthorApiModel>, t: Throwable) {
                    Log.e(
                        "PlaylistActivity/getIconUser/onFailure",
                        "Error fetching playlist: ${t.localizedMessage}"
                    )
                }
            })
    }

    private fun getVideoItems(videoId: String) {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper?.writableDatabase

        val apiServices = ApiServices.getRetrofit()
        apiServices.getStatsVideos(
            "contentDetails", "statistics", "snippet", "player",
            videoId
        )
            .enqueue(object : Callback<VideoApiModel> {
                override fun onResponse(
                    call: Call<VideoApiModel>,
                    response: Response<VideoApiModel>
                ) {
                    Log.d("PlaylistActivity/getVideoItems", "videoId: $videoId")

                    if (response.isSuccessful) {
                        val videoItems = response.body()?.items
                        videoItems?.let {
                            for (item in videoItems) {


                                val videoObj = Video(
                                    item.id,
                                    item.snippet.thumbnails.photoVideo.urlPhoto,
                                    item.contentDetails.duration,
                                    item.snippet.title,
                                    item.statistics.viewCount,
                                    item.statistics.likeCount,
                                    item.statistics.commentCount,
                                    item.snippet.publishedAt,
                                    item.player.embedHtml,
                                    item.player.embedHeight,
                                    item.player.embedWidth,
                                    AuthorVideo(
                                        authorId,
                                        authorName,
                                        authorUrlLogo,
                                        authorSubscriberCount
                                    )
                                )

                                // Save and store data in SQLite
                                val databaseVersion: String = "my_table20"
                                val insertQuery =
                                    "INSERT INTO $databaseVersion (" +
                                            "video_id, " +
                                            "urlPhotoValue, " +
                                            "durationValue, " +
                                            "titleValue, " +
                                            "viewCountValue, " +
                                            "likeCountValue, " +
                                            "commentCountValue, " +
                                            "publishedDateValue, " +
                                            "playerHtmlValue, " +
                                            "playerHeightValue, " +
                                            "playerWidthValue, " +
                                            "vidVideoValue, " +
                                            "authorVideo, " +
                                            "urlLogoValue, " +
                                            "subscriberCountValue " +
                                            ") VALUES ('" +
                                            "${videoObj.id}', " +
                                            "'${videoObj.urlPhoto}', " +
                                            "'${videoObj.duration}', " +
                                            "'${
                                                videoObj.title.replace("'", "''").replace("\"", "\"\"")
                                            }', " +
                                            "'${videoObj.viewCount}', " +
                                            "'${videoObj.likeCount}', " +
                                            "'${videoObj.commentCount}', " +
                                            "'${videoObj.publishedDate}', " +
                                            "'${videoObj.playerHtml}', " +
                                            "'${videoObj.playerHeight}', " +
                                            "'${videoObj.playerWidth}', " +
                                            "'${videoObj.authorVideo.id}', " +
                                            "'${videoObj.authorVideo.name}', " +
                                            "'${videoObj.authorVideo.urlLogo}', " +
                                            "'${videoObj.authorVideo.subscriberCount}')"

                                db?.execSQL(insertQuery)


                                // CODE REQUIRED TO RESET ALL ITEMS IN DATABASE
                                // UNCOMMENT THAT LINES AND CLICK THE BUTTON
                                // THEN SHUT DOWN YOUR APP AND COMMENT THAT LINES AGAIN

//                                  val deleteQuery = "DELETE FROM "+ databaseVersion
//                                      if (db != null) {
//                                      db.execSQL(deleteQuery)
//                                  }

                                // END OF RESTARTING DATABASE CODE


                                db?.close()

                                // End of SQLite

                                val intent = Intent(baseContext, VideoActivity::class.java)
                                val gson = Gson()
                                val json: String = gson.toJson(videoObj)
                                intent.putExtra("video", json)
                                startActivity(intent)
                            }
                        }
                    } else {
                        Log.e(
                            "PlaylistActivity/getVideoItems",
                            "Response not successful: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(call: Call<VideoApiModel>, t: Throwable) {
                    Log.e(
                        "PlaylistActivity/getVideoItems/onFailure",
                        "Error fetching playlist: ${t.localizedMessage}"
                    )
                }
            })
    }
}