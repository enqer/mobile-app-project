package com.example.flextube.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.databinding.FragmentHomeBinding
import com.example.flextube.api.ApiServices
import com.example.flextube.video.AuthorApiModel
import com.example.flextube.video.AuthorVideo
import com.example.flextube.video.Video
import com.example.flextube.VideoActivity
import com.example.flextube.video.VideoAdapter
import com.example.flextube.video.VideoApiModel
import com.example.flextube.video.VideoIdsApiModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<VideoAdapter.VideoViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    public var videosList: ArrayList<Video> = ArrayList<Video>()                // przechowuje obiekty klasy Video
    public var authorList: ArrayList<AuthorVideo> = ArrayList<AuthorVideo>()    // przechowuje obiekty klasy authorVideo
    public var idVideos: ArrayList<String> = ArrayList<String>()                // przechowuje id video
    public var idAuthors: ArrayList<String> = ArrayList<String>()              // przechowuje id twórcy powyższego video
    var idAuthorsVideos: HashMap<String, String> = HashMap<String, String>()
//    lateinit var q: String
    var iterator = 0

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val q = if (arguments?.getString("q") != null && arguments?.getString("q").toString() != "")
            arguments?.getString("q").toString()
        else
            "youtube"

        Log.i("czy działa wyszukane hasło?", q)

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)



        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mRecyclerView = _binding!!.homeRecyclerview
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

//        getMostPopularVideos()    // do poprawy albo wywalenia
        getIDsOfVideos(q)


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
                    if (vid != null && authorList.size > 0) {
                        Log.i("RETROFIT/ID", vid.items[0].id)
                        Log.i("RETROFIT/CHANNELID", authorList[iterator].id)
                        var author = authorList[iterator]
                        for( i in vid.items){
                            for (j in authorList)
                            {
                                if (j.id == idAuthorsVideos[i.id]){
                                    author = j
                                }
                            }
                            videosList.add(Video(
                                i.id,
                                i.snippet.thumbnails.photoVideo.urlPhoto,
                                i.contentDetails.duration,
                                i.snippet.title,
                                i.statistics.viewCount,
                                i.statistics.likeCount,
                                i.statistics.commentCount,
                                i.snippet.publishedAt,
                                i.player.embedHtml,
                                i.player.embedHeight,
                                i.player.embedWidth,
                                author
                            ))
                            iterator++
                        }
                    }
                }
                Log.i("RETROFIT/GETVIDEOS", "works")
                //                mRecyclerView = binding.homeRecyclerview
                mRecyclerView.setHasFixedSize(true)
//                mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                mAdapter = VideoAdapter(videosList, object : VideoAdapter.ItemClickListener{
                    override fun onItemClick(video: Video) {
//                        Toast.makeText(requireContext(), video.title,Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity?.baseContext, VideoActivity::class.java)
                        val gson = Gson()
                        val json: String = gson.toJson(video)
                        intent.putExtra("video", json)
                        startActivity(intent)
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

    private fun getIDsOfVideos(q: String){
        val api = ApiServices.getRetrofit()
        val ids: Call<VideoIdsApiModel> = api.getSearchedVideos(q=q)
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
                            idAuthorsVideos[i.id.videoId] = i.snippet.channelId
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



    // pobiera dane o filmie pojedynczo, fajnie by to było zmienić z multiple id but idk jak zrobić
    // tak bo argumenty musiałyby być zmienne to samo w api chyba że na sztywno dać po np 10 wyników
    private fun viewVideos() {
        if (idVideos.size > 0 && idAuthors.size > 0){
            for (i in idAuthors) {
                getAuthors(i)
                Log.i("view authors ", i)
            }
            for (i in idVideos)
                getVideos(i)


        }
    }

    private fun getAuthors(id: String) {
        val api = ApiServices.getRetrofit()
        val channel: Call<AuthorApiModel> = api.getChannel(id=id)
        channel.enqueue(object : Callback<AuthorApiModel>{
            override fun onResponse(
                call: Call<AuthorApiModel>,
                response: Response<AuthorApiModel>
            ) {
                if (response.isSuccessful){
                    val chan = response.body()
                    if (chan != null) {
                        for (i in chan.items){
                            authorList.add(
                                AuthorVideo(
                                    i.id,
                                    i.snippet.title,
                                    i.snippet.thumbnails.picture.url,
                                    i.statistics.subscriberCount
                                )
                            )
                            Log.i("autorzy pobierani", i.id)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<AuthorApiModel>, t: Throwable) {
                Log.i("Retrofit/IdChannel", t.stackTraceToString())
            }
        })
    }

    // dla testów do poprawy albo do wywalenia
//    private fun getMostPopularVideos(){
//        val retrofit = ApiServices.getRetrofit()
//        val mPopular: Call<VideoApiModel> = retrofit.getMostPopularVideos()
//        mPopular.enqueue(object : Callback<VideoApiModel>{
//            override fun onResponse(call: Call<VideoApiModel>, response: Response<VideoApiModel>) {
//                if (response.isSuccessful){
//                    for (i in response.body()?.items!!){
//                        getAuthors(i.snippet.channelId)
//                        addToVideosToList(response.body()!!)
//                    }
//
////                    for ((index, i) in response.body()?.items!!.withIndex()){
////                        videosList.add(Video(
////                            i.id,
////                            i.snippet.thumbnails.photoVideo.urlPhoto,
////                            i.contentDetails.duration,
////                            i.snippet.title,
////                            i.statistics.viewCount,
////                            i.statistics.likeCount,
////                            i.statistics.commentCount,
////                            i.snippet.publishedAt,
////                            i.player.embedHtml,
////                            i.player.embedHeight,
////                            i.player.embedWidth,
////                            authorList[index]
////                        ))
////                    }
//                }
////                mRecyclerView.setHasFixedSize(true)
//////                mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
////                mAdapter = VideoAdapter(videosList, object : VideoAdapter.ItemClickListener{
////                    override fun onItemClick(video: Video) {
//////                        Toast.makeText(requireContext(), video.title,Toast.LENGTH_SHORT).show()
////                        val intent = Intent(activity?.baseContext, VideoActivity::class.java)
////                        val gson = Gson()
////                        val json: String = gson.toJson(video)
////                        intent.putExtra("video", json)
////                        startActivity(intent)
////                    }
////                })
////                mRecyclerView.layoutManager=mLayoutManager
////                mRecyclerView.adapter = mAdapter
////                mAdapter.notifyDataSetChanged()
//            }
//            override fun onFailure(call: Call<VideoApiModel>, t: Throwable) {
//                Log.i("Retrofit/mostPopularVideos", t.stackTraceToString())
//            }
//        })
//    }
//
//    // dla testów do poprawy albo wywalenia
//    fun addToVideosToList(body: VideoApiModel) {
//        for ((index, i) in body?.items!!.withIndex()){
//            videosList.add(Video(
//                i.id,
//                i.snippet.thumbnails.photoVideo.urlPhoto,
//                i.contentDetails.duration,
//                i.snippet.title,
//                i.statistics.viewCount,
//                i.statistics.likeCount,
//                i.statistics.commentCount,
//                i.snippet.publishedAt,
//                i.player.embedHtml,
//                i.player.embedHeight,
//                i.player.embedWidth,
//                authorList[index]
//            ))
//        }
//        mRecyclerView.setHasFixedSize(true)
////                mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
//        mAdapter = VideoAdapter(videosList, object : VideoAdapter.ItemClickListener{
//            override fun onItemClick(video: Video) {
////                        Toast.makeText(requireContext(), video.title,Toast.LENGTH_SHORT).show()
//                val intent = Intent(activity?.baseContext, VideoActivity::class.java)
//                val gson = Gson()
//                val json: String = gson.toJson(video)
//                intent.putExtra("video", json)
//                startActivity(intent)
//            }
//        })
//        mRecyclerView.layoutManager=mLayoutManager
//        mRecyclerView.adapter = mAdapter
//        mAdapter.notifyDataSetChanged()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}