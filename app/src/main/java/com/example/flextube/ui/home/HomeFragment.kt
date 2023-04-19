package com.example.flextube.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.databinding.FragmentHomeBinding
import com.example.flextube.video.ApiServices
import com.example.flextube.video.Video
import com.example.flextube.video.VideoAdapter
import com.example.flextube.video.VideoApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<VideoAdapter.VideoViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    var videosList: ArrayList<Video> = ArrayList<Video>()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mRecyclerView = _binding!!.homeRecyclerview
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        getVideos()
        return root
    }


    private fun getVideos(){

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api: ApiServices = retrofit.create(ApiServices::class.java)

        val videos: Call<VideoApiModel> = api.getVideos()
        videos.enqueue(object : Callback<VideoApiModel>{
            override fun onResponse(call: Call<VideoApiModel>, response: Response<VideoApiModel>) {
//                val v: VideoApiModel? = response.body()
                val vid = response.body()
                //                    videosList.add(Video(v.items))
//                    videosList.add(Video(v.link,v.title,v.creatorLogo,v.creatorName,v.views, v.uploadDate))
                Log.i("RETROFIT", "works")
                if (vid != null) {
                    Log.i("YOUTUBE/", vid.items[0].snippet.title)
                }

                if (vid != null) {
                    for (i in vid.items){
                        Log.i("RETROFIT", "jest nullem")
                        Log.i("YOUTUBE/",i.snippet.title)
                        videosList.add(
                            Video(i.snippet.thumbnails.photoVideo.urlPhoto,
                                i.snippet.title,
                                "idk",
                                i.snippet.publishedAt,
                                111,
                                i.snippet.publishedAt
                            )
                        )
                    }
                }
                //                mRecyclerView = binding.homeRecyclerview
                mRecyclerView.setHasFixedSize(true)
//                mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                mAdapter = VideoAdapter(videosList)
                mRecyclerView.layoutManager=mLayoutManager
                mRecyclerView.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<VideoApiModel>, t: Throwable) {
                Log.i("RETROFIT", "no works")
                Log.i("RETROFIT", t.message.toString())
            }
        })

//        mRecyclerView = binding.homeRecyclerview
//        mRecyclerView.setHasFixedSize(true)
//        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
//        mAdapter = VideoAdapter(videosList)
//        mRecyclerView.layoutManager=mLayoutManager
//        mRecyclerView.adapter = mAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}