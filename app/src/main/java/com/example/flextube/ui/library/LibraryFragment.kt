package com.example.flextube.ui.library

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.api.ApiServices
import com.example.flextube.databinding.FragmentLibraryBinding
import com.example.flextube.playlist.Playlist
import com.example.flextube.playlist.PlaylistAdapter
import com.example.flextube.playlist.PlaylistApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.*


class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: PlaylistAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val playlistlist: ArrayList<Playlist> = ArrayList()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = PlaylistAdapter(playlistlist) // utworzenie adaptera z pustą listą
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView = binding.playlistRecyclerview.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
            setHasFixedSize(true)
        }

        getPlaylist()
    }

    private fun getPlaylist() {
        val apiServices = ApiServices.getRetrofit()
        val channelId = "UCOyHGlRFb30g-h76XiBW_pw"
        val key = ApiServices.KEY2

        apiServices.getPlaylist("snippet,contentDetails", channelId, key, null, 5)
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
                            mAdapter.notifyDataSetChanged() // zaktualizuj adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}