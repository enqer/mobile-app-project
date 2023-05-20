package com.example.flextube.playlist_item

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.flextube.R
import com.example.flextube.api.ApiServices
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlaylistActivity : AppCompatActivity() {
    private var playlistListId: String = ""
    private val playlistItemList: ArrayList<PlaylistItem> = ArrayList()

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

        Log.d(TAG, "PlaylistActivity/onCreate -> playlistListId: $playlistListId")
        Log.d(TAG, "PlaylistActivity/onCreate -> title: $title")

        playlistName.setText(title)

        getPlaylistItems()
    }

    private fun getPlaylistItems() {
        val apiServices = ApiServices.getRetrofit()
        apiServices.getPlaylistItems("snippet, contentDetails", playlistListId, 10)
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
                                val title = item.snippet.title
                                val channelTitle = item.snippet.videoOwnerChannelTitle
                                val thumbnails = item.snippet.thumbnails.medium.url
                                val channelId = item.snippet.channelId

                                Log.d(TAG, "Id: $id")
                                Log.d(TAG, "Title: $title")
                                Log.d(TAG, "channelTitle: $channelTitle")
                                Log.d(TAG, "thumbnails: $thumbnails")
                                Log.d(TAG, "channelId: $channelId")

                                playlistItemList.add(
                                    PlaylistItem(
                                        id,
                                        title,
                                        channelTitle,
                                        thumbnails,
                                        channelId
                                    )
                                )
                            }
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

}