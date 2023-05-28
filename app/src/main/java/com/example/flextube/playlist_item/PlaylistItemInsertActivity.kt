package com.example.flextube.playlist_item

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.api.ApiServices
import com.example.flextube.playlist.Playlist
import com.example.flextube.playlist.PlaylistAdapter
import com.example.flextube.playlist.PlaylistApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistItemInsertActivity : Activity() {
    private val playlistlist: ArrayList<Playlist> = ArrayList()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_item_insert)

        val videoId = intent.getStringExtra("videoId")
        val title = intent.getStringExtra("title")
        Log.d("PlaylistItemInserActivity/onCreate", "videoId: $videoId")
        Log.d("PlaylistItemInserActivity/onCreate", "title: $title")

        getPlaylist()
    }

    private fun getPlaylist() {
        val apiServices = ApiServices.getRetrofit()
        val channelId = "UCOyHGlRFb30g-h76XiBW_pw"

        apiServices.getPlaylist("snippet,contentDetails", channelId, null, 5)
            .enqueue(object : Callback<PlaylistApiModel> {
                override fun onResponse(
                    call: Call<PlaylistApiModel>,
                    response: Response<PlaylistApiModel>
                ) {
                    if (response.isSuccessful) {
                        val playlistItems = response.body()?.items

                        playlistItems?.let {
                            for (item in playlistItems) {
                                val id = item.id
                                val title = item.snippetYt.title
                                val desciption = item.snippetYt.description
                                val thumbnailsUrl = item.snippetYt.thumbnails.medium.url
                                val itemCount = item.contentDetail.itemCount

                                // Just check that everything is fine
                                Log.d(ContentValues.TAG, "Id: $id")
                                Log.d(ContentValues.TAG, "Title: $title")
                                Log.d(ContentValues.TAG, "Description: $desciption")
                                Log.d(ContentValues.TAG, "Thumbnails: $thumbnailsUrl")
                                Log.d(ContentValues.TAG, "ItemCount: $itemCount")

                                // Adding stuff (important)
                                playlistlist.add(
                                    Playlist(
                                        id,
                                        title,
                                        desciption,
                                        thumbnailsUrl,
                                        itemCount
                                    )
                                )
                            }
                            // Makes the items clickable and move to different activity from fragment
                            mAdapter = PlaylistAdapter(
                                playlistlist,
                                object : PlaylistAdapter.ItemClickListener {
                                    override fun onItemClick(playlist: Playlist) {
                                        Log.d(
                                            ContentValues.TAG,
                                            "LibraryFragment/getPlaylist/onItemClick -> Item clicked"
                                        )

                                    }
                                })
//                            mLayoutManager = LinearLayoutManager(requireContext())
//                            mRecyclerView = binding.playlistRecyclerview.apply {
//                                layoutManager = mLayoutManager
//                                adapter = mAdapter
//                                setHasFixedSize(true)
//
//                                mAdapter.notifyDataSetChanged() // update adapter
//                            }
                        }
                    } else {
                        Log.e(ContentValues.TAG, "Response not successful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PlaylistApiModel>, t: Throwable) {
                    Log.e(ContentValues.TAG, "Error fetching playlist: ${t.localizedMessage}")
                }
            })
    }
}
