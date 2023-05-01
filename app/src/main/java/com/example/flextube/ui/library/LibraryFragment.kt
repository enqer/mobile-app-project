package com.example.flextube.ui.library

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val playlists: MutableList<Playlist> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(LibraryViewModel::class.java)

        Log.e(TAG, "test")

        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val playlistRecyclerView = root.findViewById(R.id.playlist_recyclerview) as RecyclerView

        //val playlistRecyclerView = binding.playlistRecyclerView
        val adapter = PlaylistAdapter(playlists)
        playlistRecyclerView.adapter = adapter


        getPlaylist()



        return root
    }


    val apiServices = ApiServices.getRetrofit()
    val channelId = "UCOyHGlRFb30g-h76XiBW_pw"
    val key = ApiServices.KEY2
    fun getPlaylist() {
        apiServices.getPlaylist("snippet,contentDetails", channelId, key, null, null)
            .enqueue(object : Callback<PlaylistApiModel> {
                override fun onResponse(
                    call: Call<PlaylistApiModel>,
                    response: Response<PlaylistApiModel>
                ) {
                    if (response.isSuccessful) {
                        Log.i("RETROFIT", "getPlaylist")
                        val playlistItems = response.body()?.items
                        playlistItems?.let {
                            for (item in playlistItems) {
                                val title = item.snippetYt.title
                                val desciption = item.snippetYt.description
                                val thumbnails = item.snippetYt.thumbnails.high.url
                                Log.d(TAG, "Title: $title")
                                Log.d(TAG, "description: $desciption")
                                Log.d(TAG, "thumbnails: $thumbnails")
                                //Log.d(TAG, "id: $item.id")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
