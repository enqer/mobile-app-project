package com.example.flextube.ui.shorts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.api.ApiServices
import com.example.flextube.databinding.FragmentShortsBinding
import com.example.flextube.shorts.Shorts
import com.example.flextube.shorts.ShortsAdapter
import com.example.flextube.shorts.ShortsItem
import com.example.flextube.video.AuthorVideo
import com.example.flextube.video.ShortsApiModel
import com.example.flextube.video.Video
import com.example.flextube.video.VideoApiModel
import com.example.flextube.video.VideoIdsApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShortsFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    var Shortlist: ArrayList<Shorts> = ArrayList<Shorts>()
    public var videosList: ArrayList<Video> = ArrayList<Video>()
    var idVideos: ArrayList<String> = ArrayList()
    var authorList: ArrayList<AuthorVideo> = ArrayList<AuthorVideo>()
    var idAuthors: ArrayList<String> = ArrayList()
    val titleList : ArrayList<String> = ArrayList()

    private var _binding: FragmentShortsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ShortsViewModel::class.java)

        _binding = FragmentShortsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mRecyclerView = _binding!!.shortsRecyclerview
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        val snapHelper = LinearSnapHelper()

        snapHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = mLayoutManager

        ///endless recycler view endless scrolling
//        val sh= ShortsItem("https://pbs.twimg.com/profile_images/556495456805453826/wKEOCDN0_400x400.png","Dudu","34","34")
//        Shortlist.add(sh)
//
//        val sh1= ShortsItem("https://upload.wikimedia.org/wikipedia/commons/c/c3/Jaros%C5%82aw_Kaczy%C5%84ski%2C_wicepremier_%28cropped%29.png","Kaczor","34","34")
//        Shortlist.add(sh1)
//        val sh2= ShortsItem("https://upload.wikimedia.org/wikipedia/commons/5/5d/Mateusz_Morawiecki_Prezes_Rady_Ministr%C3%B3w_%28cropped%29.jpg","Mati","34","34")
//        Shortlist.add(sh2)
//
//        val sh3= ShortsItem("https://www.malopolska.pl/_cache/councilors/790-790/fit/DudaJ.jpg","Old dudu","34","34")
//        Shortlist.add(sh3)


        getIDsOfVideos()

//        mAdapter = ShortsAdapter(Short)
//
        //mRecyclerView.adapter = mAdapter
//        mAdapter.notifyDataSetChanged()

        return root
    }

    private fun getIDsOfVideos(){
        val api = ApiServices.getRetrofit()
        val ids: Call<VideoIdsApiModel> = api.getShorts()
        Log.i("RETROFIT", "getID")
        ids.enqueue(object : Callback<VideoIdsApiModel>{
            override fun onResponse(call: Call<VideoIdsApiModel>, response: Response<VideoIdsApiModel>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null) {
                        for (i in body.items){
                            idVideos.add(i.id.videoId)
                            idAuthors.add(i.snippet.channelId)
                            Log.i("video id", i.id.videoId)
                            Log.i("channel id", i.snippet.channelId)
                        }
                        viewVideos()
                    }
                }
            }
            override fun onFailure(call: Call<VideoIdsApiModel>, t: Throwable) {
                Log.i("Retrofit/Id", t.stackTraceToString())
            }
        })
        Log.i("dziala","dfd")
    }

    private fun viewVideos() {
            for (i in idVideos)
                getVideos(i)
    }
    private fun getVideos(id: String){
        val api = ApiServices.getRetrofit()
        val videos: Call<ShortsApiModel> = api.getStatsShorts(id=id)
        videos.enqueue(object: Callback<ShortsApiModel>{
            override fun onResponse(call: Call<ShortsApiModel>, response: Response<ShortsApiModel>) {
                if(response.isSuccessful){
                    val vid = response.body()
                    if (vid != null) {
                        for ( i in vid.items){
                            Shortlist.add(Shorts(
                                i.id,
                                i.snippet.title,
                                i.snippet.channelTitle,
                                i.snippet.thumbnails.photoVideo.urlPhoto,
                                i.statistics.likeCount,
                                i.statistics.dislikeCount
                            ))

                        Log.i("tak",i.snippet.title)
                        Log.i("tak", i.statistics.likeCount.toString())
                        Log.i("tak",i.snippet.channelTitle)
                        }
                    }
                    mRecyclerView.setHasFixedSize(true)
                    mAdapter = ShortsAdapter(Shortlist)
                    mRecyclerView.layoutManager=mLayoutManager
                    mRecyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()
                }
            }


            override fun onFailure(call: Call<ShortsApiModel>, t: Throwable) {
                Log.i("onFailure","błąd")
            }
        })
        return
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


