package com.example.flextube.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.databinding.FragmentHomeBinding
import com.example.flextube.video.ApiServices
import com.example.flextube.video.Video
import com.example.flextube.video.VideoAdapter
import com.example.flextube.video.VideoApiModel
import com.example.flextube.video.VideoIdsApiModel
import com.example.flextube.video.VideoStatsApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<VideoAdapter.VideoViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    public var videosList: ArrayList<Video> = ArrayList<Video>()        // przechowuje obiekty klasy Video
    public var idVideos: ArrayList<String> = ArrayList<String>()        // przechowuje id video
    public var idChannels: ArrayList<String> = ArrayList<String>()      // przechowuje id twórcy powyższego video

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


        getIDsOfVideos()

        return root
    }


    private fun getVideos(id: String){
        val api = ApiServices.getRetrofit()
        val videos: Call<VideoApiModel> = api.getStatsVideos(id=id)
        Log.i("RETROFIT", "getVideos")
        videos.enqueue(object : Callback<VideoApiModel>{
            override fun onResponse(call: Call<VideoApiModel>, response: Response<VideoApiModel>) {
                if (response.isSuccessful){
                    val vid = response.body()
                    Log.i("RETROFIT", "works")
                    if (vid != null) {
                        Log.i("RETROFIT/ID", vid.items[0].id)
                        for(i in vid.items){
                            videosList.add(Video(
                                i.id,
                                i.snippet.thumbnails.photoVideo.urlPhoto,
                                i.contentDetails.duration,
                                i.snippet.title,
                                "creatorLogo do zrobienia",
                                i.snippet.channelTitle,
                                i.statistics.viewCount,
                                i.statistics.likeCount,
                                i.statistics.commentCount,
                                i.snippet.publishedAt
                            ))
                        }
                    }
                }
                Log.i("RETROFIT/GETVIDEOS", "works")
                //                mRecyclerView = binding.homeRecyclerview
                mRecyclerView.setHasFixedSize(true)
//                mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                mAdapter = VideoAdapter(videosList, object : VideoAdapter.ItemClickListener{
                    override fun onItemClick(video: Video) {
                        Toast.makeText(requireContext(), video.title,Toast.LENGTH_SHORT).show()
                    }
                })
                mRecyclerView.layoutManager=mLayoutManager
                mRecyclerView.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<VideoApiModel>, t: Throwable) {
                Log.i("RETROFIT/VIDEOS", "no works")
                Log.i("RETROFIT/VIDEOS", t.message.toString())
            }
        })
        return
    }

    private fun getIDsOfVideos(){
        val api = ApiServices.getRetrofit()
        val ids: Call<VideoIdsApiModel> = api.getVideos()
        Log.i("RETROFIT", "getID")
        ids.enqueue(object : Callback<VideoIdsApiModel>{
            override fun onResponse(call: Call<VideoIdsApiModel>, response: Response<VideoIdsApiModel>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null) {
                        for (i in body.items){
                            idVideos.add(i.id.videoId)
                            Log.i("RETROFIT", i.id.videoId)
                        }
                        viewVideos()
                    }
                }
            }
            override fun onFailure(call: Call<VideoIdsApiModel>, t: Throwable) {
                Log.i("Retrofit/Id", t.stackTraceToString())
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun viewVideos() {
        if (idVideos.size > 0){
            for (i in idVideos){
                getVideos(i)
            }
        }
    }
}