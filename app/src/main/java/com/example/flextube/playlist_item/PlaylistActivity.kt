package com.example.flextube.playlist_item

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.api.ApiServices
import com.example.flextube.shorts.ShortsApiModel
import com.example.flextube.shorts.ShortsAuthor
import com.example.flextube.shorts.ShortsAuthorApiModel

import com.google.android.gms.auth.api.signin.GoogleSignIn
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

    private var videoOwnerChannelId: String = ""

    private val channelList: ArrayList<ShortsAuthor> = ArrayList()
//    private lateinit var qLayoutManager: RecyclerView.LayoutManager
//    private lateinit var qRecyclerView: RecyclerView
//    private lateinit var qAdapter: RecyclerView.Adapter<ChannelsAdapter.ChannelItemsViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        Log.d(TAG,"PLaylistActivity/onCreate -> Start activity")

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
        mAdapter = PlaylistItemAdapter(playlistItemList, channelList, object : PlaylistItemAdapter.ItemClickListener {
            override fun onItemClick(playlistItem: PlaylistItem) {
                // Obsługa kliknięcia na element listy
                Log.d(TAG, "click click click")
            }

            override fun onChannelItemClick(channelItem: ShortsAuthor) {
                // Obsługa kliknięcia na ikonkę kanału
                Log.d(TAG, "click click click")
            }
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
        apiServices.getPlaylistItems("snippet, contentDetails", playlistListId, ApiServices.KEY2, 10)
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
                                videoOwnerChannelId = item.snippet.videoOwnerChannelId

                                if(title.length > maxLength){
                                    title = title.substring(0, maxLength) + "..."
                                }
                                if(channelTitle.length > maxLength){
                                    channelTitle =channelTitle.substring(0, maxLength) + "..."
                                }

                                Log.d(TAG, "Id: $id")
                                Log.d(TAG, "Title: $title")
                                Log.d(TAG, "channelTitle: $channelTitle")
                                Log.d(TAG, "thumbnails: $thumbnails")
                                Log.d(TAG, "videoOwnerChannelId: $videoOwnerChannelId")

                                getIconUser()

                                playlistItemList.add(
                                    PlaylistItem(
                                        id,
                                        title,
                                        channelTitle,
                                        thumbnails,
                                        videoOwnerChannelId
                                    )
                                )
                            }

//                            mAdapter = PlaylistItemAdapter(
//                                playlistItemList,
//                                object : PlaylistItemAdapter.ItemClickListener {
//                                    override fun onItemClick(playlistItem: PlaylistItem) {
//                                        Log.d(TAG, "click click click")
//                                }
//                            })
//
//                            mLayoutManager = LinearLayoutManager(this@PlaylistActivity)
//                            mRecyclerView = findViewById(R.id.playlist_items_recyclerview)
//                            mRecyclerView.layoutManager = mLayoutManager
//                            mRecyclerView.adapter = mAdapter
//                            mRecyclerView.setHasFixedSize(true)
//
//                            mAdapter.notifyDataSetChanged() // Aktualizacja adaptera

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
    private fun getIconUser() {
        val apiServices = ApiServices.getRetrofit()
        apiServices.getShortsChannel("snippet", "statistics", videoOwnerChannelId, ApiServices.KEY2)
            .enqueue(object : Callback<ShortsAuthorApiModel> {
                override fun onResponse(
                    call: Call<ShortsAuthorApiModel>,
                    response: Response<ShortsAuthorApiModel>
                ) {
                    if (response.isSuccessful) {
                        val channelItem = response.body()?.items

                        channelItem?.let {
                            for (item in channelItem) {
                                val id = item.id
                                val urlLogo = item.snippet.thumbnails.picture.url

                                Log.d(TAG, "id: $id")
                                Log.d(TAG, "urlLogo: $urlLogo")

                                channelList.add(
                                    ShortsAuthor(
                                        id,
                                        urlLogo
                                    )
                                )
                            }
                            mAdapter.notifyDataSetChanged()
//                            qAdapter = ChannelsAdapter(
//                                channelList,
//                                object : ChannelsAdapter.ItemClickListener {
//                                    override fun onItemClick(channelItem: ShortsApiModel.Shorts) {
//                                        Log.d(TAG, "click click click")
//                                    }
//                                }
//                            )
//
//                            qLayoutManager = LinearLayoutManager(this@PlaylistActivity)
//                            qRecyclerView = findViewById(R.id.playlist_items_recyclerview)
//                            qRecyclerView.layoutManager = qLayoutManager
//                            qRecyclerView.adapter = qAdapter
//                            qRecyclerView.setHasFixedSize(true)
                        }
                    } else {
                        Log.e(TAG, "Response not successful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ShortsAuthorApiModel>, t: Throwable) {
                    Log.e(TAG, "Error fetching playlist: ${t.localizedMessage}")
                }

            })
    }

}
