package com.example.flextube.ui.library

import android.R.id.message
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.flextube.api.ApiServices
import com.example.flextube.databinding.FragmentLibraryBinding
import com.example.flextube.playlist.Playlist
import com.example.flextube.playlist.PlaylistAdapter
import com.example.flextube.playlist.PlaylistApiModel
import com.example.flextube.playlist_item.PlaylistActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    //private lateinit var mAdapter: PlaylistAdapter
    private lateinit var mAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>
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

        getPlaylist()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mAdapter = PlaylistAdapter(playlistlist, 0, 0) // utworzenie adaptera z pustą listą
//        mLayoutManager = LinearLayoutManager(requireContext())
//        mRecyclerView = binding.playlistRecyclerview.apply {
//            layoutManager = mLayoutManager
//            adapter = mAdapter
//            setHasFixedSize(true)
//        }

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

                                Log.d(TAG, "Id: $id")
                                Log.d(TAG, "Title: $title")
                                Log.d(TAG, "Description: $desciption")
                                Log.d(TAG, "Thumbnails: $thumbnailsUrl")
                                Log.d(TAG, "ItemCount: $itemCount")

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
                            mAdapter = PlaylistAdapter(playlistlist, object : PlaylistAdapter.ItemClickListener{
                                override fun onItemClick(playlist: Playlist) {
                                    Log.d(TAG, "LibraryFragment/getPlaylist/onItemClick -> Item clicked")
//                                    val intent = Intent(
//                                        activity!!.baseContext,
//                                        PlaylistActivity::class.java
//                                    )
                                    val startNew = Intent(context, PlaylistActivity::class.java)

                                    startNew.putExtra("message", playlist.id)
                                    activity!!.startActivity(startNew)


                                    //Toast.makeText(requireContext(), playlist.title,Toast.LENGTH_SHORT).show()
                                }
                            })
                            mLayoutManager = LinearLayoutManager(requireContext())
                            mRecyclerView = binding.playlistRecyclerview.apply {
                                layoutManager = mLayoutManager
                                adapter = mAdapter
                                setHasFixedSize(true)

                                mAdapter.notifyDataSetChanged() // zaktualizuj adapter
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