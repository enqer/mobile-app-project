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
import com.example.flextube.database.SQLiteManager
import com.example.flextube.databinding.FragmentLibraryBinding
import com.example.flextube.history.HistoryAdapter
import com.example.flextube.playlist.Playlist
import com.example.flextube.playlist.PlaylistAdapter
import com.example.flextube.playlist.PlaylistApiModel
import com.example.flextube.playlist_item.PlaylistActivity
import com.example.flextube.video.Video
import com.example.flextube.video.VideoActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// Start LibraryFragment
class LibraryFragment : Fragment() {

    private lateinit var sqLiteManager: SQLiteManager

    private var _binding: FragmentLibraryBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val playlistlist: ArrayList<Playlist> = ArrayList()
    private val DARK_MODE = "darkMode"

    private lateinit var qRecyclerView: RecyclerView
    private lateinit var qAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>
    private lateinit var qLayoutManager: RecyclerView.LayoutManager
    private var historyList: ArrayList<Video> = ArrayList()


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        sqLiteManager = SQLiteManager(requireActivity().baseContext)
        
        showHistory()
        getPlaylist()

        return binding.root
    }

    private fun showHistory() {
        for (i in sqLiteManager.getVideosHistory())
            historyList.add(0, i)
        qAdapter = HistoryAdapter(
            historyList,
            object : HistoryAdapter.ItemClickListener {
                override fun onItemClick(history: Video) {


                    val intent = Intent(activity?.baseContext, VideoActivity::class.java)
                    val gson = Gson()
                    val json: String = gson.toJson(history)
                    intent.putExtra("video", json)
                    startActivity(intent)
                }
            })
        qLayoutManager = LinearLayoutManager(requireContext())
        (qLayoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.HORIZONTAL

        qRecyclerView = binding.historyRecyclerview.apply {

            layoutManager = qLayoutManager
            adapter = qAdapter
            setHasFixedSize(true)

            qAdapter.notifyDataSetChanged() // update adapter

        }
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