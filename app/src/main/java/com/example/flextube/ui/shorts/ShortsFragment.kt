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
import com.example.flextube.interfaces.Formatter
import com.example.flextube.shorts.Author
import com.example.flextube.shorts.Shorts
import com.example.flextube.shorts.ShortsAdapter
import com.example.flextube.shorts.ShortsApiModel
import com.example.flextube.shorts.ShortsAuthor
import com.example.flextube.shorts.ShortsAuthorApiModel
import com.example.flextube.video.VideoIdsApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShortsFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    var shortList: ArrayList<Shorts> = ArrayList<Shorts>()
    var idVideos: ArrayList<String> = ArrayList()
    var authorList: ArrayList<ShortsAuthor> = ArrayList<ShortsAuthor>()
    var idAuthors: ArrayList<String> = ArrayList()
    val authorsLogosList : ArrayList<String> = ArrayList()
    var idAuthorsVideos: HashMap<String, String> = HashMap<String, String>()
    var index = 0


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
        Log.d("dd",ApiServices.authToken)
        val snapHelper: LinearSnapHelper = object : LinearSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
                val position = layoutManager.getPosition(centerView)
                var targetPosition = -1
                if (layoutManager.canScrollHorizontally()) {
                    targetPosition = if (velocityX < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                if (layoutManager.canScrollVertically()) {
                    targetPosition = if (velocityY < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                val firstItem = 0
                val lastItem = layoutManager.itemCount - 1
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem))
                return targetPosition
            }
        }
        snapHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = mLayoutManager

        getIDsOfVideos()
        Log.i("lista short", shortList.size.toString())
        Log.i("lista title", authorsLogosList.size.toString())

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
        Log.i("dziala","dfd")
    }
    private fun getAuthors(id:String){
        val api = ApiServices.getRetrofit()
        val channel: Call<ShortsAuthorApiModel> = api.getShortsChannel(id =id)
        channel.enqueue(object : Callback<ShortsAuthorApiModel>{
            override fun onResponse(
                call: Call<ShortsAuthorApiModel>,
                response: Response<ShortsAuthorApiModel>
            ) {
                if (response.isSuccessful){
                    val chan = response.body()
                    if (chan != null) {
                        for (i in chan.items){
                            authorList.add(ShortsAuthor(
                                i.id,
                                i.snippet.thumbnails.picture.url
                            )

                            )
                            Log.i("logo", i.snippet.thumbnails.picture.url)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ShortsAuthorApiModel>, t: Throwable) {
                Log.i("Retrofit/IdChannel", t.stackTraceToString())
            }
        })
    }

    private fun viewVideos() {

            for (i in idAuthors) {
                getAuthors(i)
            }
            for (i in idVideos)
                getVideos(i)

        }
    private fun getVideos(id: String){
        val api = ApiServices.getRetrofit()
        val videos: Call<ShortsApiModel> = api.getStatsShorts(id=id)
        videos.enqueue(object: Callback<ShortsApiModel>{
            override fun onResponse(call: Call<ShortsApiModel>, response: Response<ShortsApiModel>) {
                if (response.isSuccessful) {
                    val vid = response.body()
                    if (vid != null && authorList.size > 0) {
                        var author = authorList[index]
                            for (i in vid.items) {
                                for (j in authorList)
                                {
                                    if (j.id == idAuthorsVideos[i.id]){
                                        author = j
                                    }
                                }
                                shortList.add(
                                    Shorts(
                                        i.player.embedHtml,
                                        i.id,
                                        i.snippet.title,
                                        i.snippet.channelTitle,
                                        i.snippet.channelId,
                                        author.urlLogo,
                                        Formatter.formatNumber(i.statistics.likeCount,requireContext()),
                                        i.statistics.dislikeCount
                                    )
                                )
                                //Log.i("t",authorsLogosList[index])
                                index++
                            }
                    }
                }
                    mRecyclerView.setHasFixedSize(true)
                    mAdapter = ShortsAdapter(shortList)
                    mRecyclerView.layoutManager=mLayoutManager
                    mRecyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()
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


