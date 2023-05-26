package com.example.flextube.ui.library

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.api.ApiServices
import com.example.flextube.database.DatabaseHelper
import com.example.flextube.databinding.FragmentLibraryBinding
import com.example.flextube.history.HistoryAdapter
import com.example.flextube.playlist.Playlist
import com.example.flextube.playlist.PlaylistAdapter
import com.example.flextube.playlist.PlaylistApiModel
import com.example.flextube.playlist_item.PlaylistActivity
import com.example.flextube.video.AuthorVideo
import com.example.flextube.video.Video
import com.example.flextube.video.VideoActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// Start LibraryFragment
class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val playlistlist: ArrayList<Playlist> = ArrayList()
    private val DARK_MODE = "darkMode"

    private lateinit var qRecyclerView: RecyclerView
    private lateinit var qAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>
    private lateinit var qLayoutManager: RecyclerView.LayoutManager
    private val historyList: ArrayList<Video> = ArrayList()


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        readDatabase()
        getPlaylist()
        return binding.root
    }

    private fun readDatabase() {
        val databaseVersion: String = "my_table20"
        val dbHelper = context?.let { DatabaseHelper(it) }
        val db = dbHelper?.writableDatabase

        val selectQuery = "SELECT * FROM $databaseVersion ORDER BY id DESC"

        val cursor = db?.rawQuery(selectQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (cursor != null) {
                    do {
                        val idIndex = cursor.getColumnIndex("video_id")
                        val idValue = cursor.getString(idIndex)

                        val urlPhotoValueIndex = cursor.getColumnIndex("urlPhotoValue")
                        val urlPhotoValue = cursor.getString(urlPhotoValueIndex)

                        val durationIndex = cursor.getColumnIndex("durationValue")
                        val durationValue = cursor.getString(durationIndex)

                        val titleValueIndex = cursor.getColumnIndex("titleValue")
                        val titleValue = cursor.getString(titleValueIndex)

                        val viewCountValueIndex = cursor.getColumnIndex("viewCountValue")
                        val viewCountValue = cursor.getString(viewCountValueIndex)

                        val likeCountValueIndex = cursor.getColumnIndex("likeCountValue")
                        val likeCountValue = cursor.getString(likeCountValueIndex)

                        val commentCountIndex = cursor.getColumnIndex("commentCountValue")
                        val commentCountValue = cursor.getString(commentCountIndex)

                        val publishedDateIndex = cursor.getColumnIndex("publishedDateValue")
                        val publishedDateValue = cursor.getString(publishedDateIndex)

                        val playerHtmlIndex = cursor.getColumnIndex("playerHtmlValue")
                        val playerHtmlValue = cursor.getString(playerHtmlIndex)

                        val playerHeightIndex = cursor.getColumnIndex("playerHeightValue")
                        val playerHeightValue = cursor.getLong(playerHeightIndex)

                        val playerWidthIndex = cursor.getColumnIndex("playerWidthValue")
                        val playerWidthValue = cursor.getLong(playerWidthIndex)

                        val vidVideoValueIndex = cursor.getColumnIndex("vidVideoValue")
                        val vidVideoValue = cursor.getString(vidVideoValueIndex)

                        val authorVideoIndex = cursor.getColumnIndex("authorVideo")
                        val authorVideoValue = cursor.getString(authorVideoIndex)

                        val urlLogoValueIndex = cursor.getColumnIndex("urlLogoValue")
                        val urlLogoValue = cursor.getString(urlLogoValueIndex)

                        val subscriberCountValueIndex = cursor.getColumnIndex("subscriberCountValue")
                        val subscriberCountValue = cursor.getString(subscriberCountValueIndex)
                        Log.d(
                            "MyApp",
                            "Pobrana wartość: " +
                                    "$idValue, " +
                                    "$urlPhotoValue, " +
                                    "$durationValue, " +
                                    "$titleValue, " +
                                    "$viewCountValue, " +
                                    "$likeCountValue, " +
                                    "$commentCountValue, " +
                                    "$publishedDateValue, " +
                                    "$playerHtmlValue, " +
                                    "$playerHeightValue, " +
                                    "$playerWidthValue, " +
                                    "$vidVideoValue, " +
                                    "$authorVideoValue, " +
                                    "$urlLogoValue, " +
                                    "$subscriberCountValue ,"
                        )
                        historyList.add(
                            Video(
                                idValue,
                                urlPhotoValue,
                                durationValue,
                                //"0:20",
                                titleValue,
                                viewCountValue,
                                likeCountValue,
                                commentCountValue,
                                publishedDateValue,
                                playerHtmlValue,
                                playerHeightValue,
                                playerWidthValue,
                                AuthorVideo(
                                    vidVideoValue,
                                    authorVideoValue,
                                    urlLogoValue,
                                    subscriberCountValue
                                )
                            )
                        )
                        // Makes the items clickable and move to different activity from fragment
                        qAdapter = HistoryAdapter(
                            historyList,
                            object : HistoryAdapter.ItemClickListener {
                                override fun onItemClick(history: Video) {
                                    Log.d(
                                        TAG,
                                        "LibraryFragment/readDatabase/onItemClick -> Item clicked"
                                    )

                                    val intent = Intent(activity?.baseContext, VideoActivity::class.java)
                                    val gson = Gson()
                                    val json: String = gson.toJson(history)
                                    intent.putExtra("video", json)
                                    startActivity(intent)

                                    //Toast.makeText(requireContext(), playlist.title,Toast.LENGTH_SHORT).show()
                                }
                            })
                        qLayoutManager = LinearLayoutManager(requireContext())
                        (qLayoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL // dodaj tę linię

                        qRecyclerView = binding.historyRecyclerview.apply {

                            layoutManager = qLayoutManager
                            adapter = qAdapter
                            setHasFixedSize(true)

                            qAdapter.notifyDataSetChanged() // update adapter

                        }


                    } while (cursor.moveToNext())
                }
            }
        }


        if (cursor != null) {
            cursor.close()
        }
//        if (dbHelper != null) {
//            dbHelper.close()
//        }

    }

    // Search available playlist on channel using channelId then show them
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
                                Log.d(TAG, "Id: $id")
                                Log.d(TAG, "Title: $title")
                                Log.d(TAG, "Description: $desciption")
                                Log.d(TAG, "Thumbnails: $thumbnailsUrl")
                                Log.d(TAG, "ItemCount: $itemCount")

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
                                            TAG,
                                            "LibraryFragment/getPlaylist/onItemClick -> Item clicked"
                                        )

                                        // Starts new activity
                                        val startNew = Intent(context, PlaylistActivity::class.java)

                                        startNew.putExtra("id", playlist.id)
                                        startNew.putExtra("title", playlist.title)
                                        activity!!.startActivity(startNew)

                                        //Toast.makeText(requireContext(), playlist.title,Toast.LENGTH_SHORT).show()
                                    }
                                })
                            mLayoutManager = LinearLayoutManager(requireContext())
                            mRecyclerView = binding.playlistRecyclerview.apply {
                                layoutManager = mLayoutManager
                                adapter = mAdapter
                                setHasFixedSize(true)

                                mAdapter.notifyDataSetChanged() // update adapter
                            }
                        }
                    } else {
                        Log.e(TAG, "Response not successful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PlaylistApiModel>, t: Throwable) {
                    Log.e(TAG, "Error fetching playlist: ${t.localizedMessage}")
                }
            })
    }

    // Execute order 66
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}