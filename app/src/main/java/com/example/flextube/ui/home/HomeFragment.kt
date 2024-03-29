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
import com.example.flextube.api.ApiServices
import com.example.flextube.database.SQLiteManager
import com.example.flextube.databinding.FragmentHomeBinding
import com.example.flextube.interfaces.Formatter
import com.example.flextube.video.AuthorApiModel
import com.example.flextube.video.AuthorVideo
import com.example.flextube.video.Video
import com.example.flextube.video.VideoActivity
import com.example.flextube.video.VideoAdapter
import com.example.flextube.video.VideoApiModel
import com.example.flextube.video.VideoIdsApiModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var sqLiteManager: SQLiteManager   //SQL
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<VideoAdapter.VideoViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    public var videosList: ArrayList<Video> =
        ArrayList<Video>()                // przechowuje obiekty klasy Video
    public var authorList: ArrayList<AuthorVideo> =
        ArrayList<AuthorVideo>()    // przechowuje obiekty klasy authorVideo
    public var idVideos: ArrayList<String> =
        ArrayList<String>()                // przechowuje id video
    public var idAuthors: ArrayList<String> =
        ArrayList<String>()              // przechowuje id twórcy powyższego video
    var idAuthorsVideos: HashMap<String, String> = HashMap<String, String>()
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

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        sqLiteManager = SQLiteManager(requireActivity().baseContext)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mRecyclerView = binding.homeRecyclerview
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // getting id channels and videos of searching videos
        getIDsOfVideos(q)


        //getIDsOfVideos()


        return root
    }


    // getting videos by id video
    private fun getVideos(id: String) {
        // Variables used in SQLite stuff
//        val dbHelper = context?.let { DatabaseHelper(it) }
//        val db = dbHelper?.writableDatabase

        val api = ApiServices.getRetrofit()
        val videos: Call<VideoApiModel> = api.getStatsVideos(id = id)
        videos.enqueue(object : Callback<VideoApiModel> {
            override fun onResponse(call: Call<VideoApiModel>, response: Response<VideoApiModel>) {
                if (response.isSuccessful) {
                    val vid = response.body()
                    if (vid != null && authorList.size > 0) {
                        var author = authorList[iterator]
                        for (i in vid.items) {
                            for (j in authorList) {
                                if (j.id == idAuthorsVideos[i.id]) {
                                    author = j
                                }
                            }

                            videosList.add(Video(
                                i.id,
                                i.snippet.thumbnails.photoVideo.urlPhoto,
                                i.contentDetails.duration,
                                i.snippet.title,
                                Formatter.formatNumber(i.statistics.viewCount,requireContext()),
                                Formatter.formatNumber(i.statistics.likeCount,requireContext()),
                                Formatter.formatNumber(i.statistics.commentCount,requireContext()),
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
                //                mRecyclerView = binding.homeRecyclerview
                mRecyclerView.setHasFixedSize(true)
//                mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                mAdapter = VideoAdapter(videosList, object : VideoAdapter.ItemClickListener {
                    override fun onItemClick(video: Video) {
//                        Toast.makeText(requireContext(), video.title,Toast.LENGTH_SHORT).show()
//
//                        Log.d("HomeFragment/getVideos/onItemClick -> video.id", video.id)
//
//                        // Save and store data in SQLite
//                        val databaseVersion: String = "my_table20"
//                        val insertQuery =
//                            "INSERT INTO $databaseVersion (" +
//                                    "video_id, " +
//                                    "urlPhotoValue, " +
//                                    "durationValue, " +
//                                    "titleValue, " +
//                                    "viewCountValue, " +
//                                    "likeCountValue, " +
//                                    "commentCountValue, " +
//                                    "publishedDateValue, " +
//                                    "playerHtmlValue, " +
//                                    "playerHeightValue, " +
//                                    "playerWidthValue, " +
//                                    "vidVideoValue, " +
//                                    "authorVideo, " +
//                                    "urlLogoValue, " +
//                                    "subscriberCountValue " +
//                                    ") VALUES ('" +
//                                    "${video.id}', " +
//                                    "'${video.urlPhoto}', " +
//                                    "'${video.duration}', " +
//                                    "'${
//                                        video.title.replace("'", "''").replace("\"", "\"\"")
//                                    }', " +
//                                    "'${video.viewCount}', " +
//                                    "'${video.likeCount}', " +
//                                    "'${video.commentCount}', " +
//                                    "'${video.publishedDate}', " +
//                                    "'${video.playerHtml}', " +
//                                    "'${video.playerHeight}', " +
//                                    "'${video.playerWidth}', " +
//                                    "'${video.authorVideo.id}', " +
//                                    "'${video.authorVideo.name}', " +
//                                    "'${video.authorVideo.urlLogo}', " +
//                                    "'${video.authorVideo.subscriberCount}')"
//
//                        db?.execSQL(insertQuery)

                        // CODE REQUIRED TO RESET ALL ITEMS IN DATABASE
                        // UNCOMMENT THAT LINES AND CLICK THE BUTTON
                        // THEN SHUT DOWN YOUR APP AND COMMENT THAT LINES AGAIN

//                        val deleteQuery = "DELETE FROM "+ databaseVersion
//                        if (db != null) {
//                            db.execSQL(deleteQuery)
//                        }

                        // END OF RESTARTING DATABASE CODE


//                        db?.close()

                        // End of SQLite
                        sqLiteManager.insertVideo(video)

                        val intent = Intent(activity?.baseContext, VideoActivity::class.java)
                        val gson = Gson()
                        val json: String = gson.toJson(video)
                        intent.putExtra("video", json)
                        startActivity(intent)
                    }
                })
                mRecyclerView.layoutManager = mLayoutManager
                mRecyclerView.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<VideoApiModel>, t: Throwable) {
                Log.i("RETROFIT/VIDEOS", "no works")
                Log.i("RETROFIT/VIDEOS", t.message.toString())
            }
        })
    }

    // getting id channels and videos (after that we can searching for full data of video)
    private fun getIDsOfVideos(q: String) {
        val api = ApiServices.getRetrofit()
        val ids: Call<VideoIdsApiModel> = api.getSearchedVideos(q = q)
        ids.enqueue(object : Callback<VideoIdsApiModel> {
            override fun onResponse(
                call: Call<VideoIdsApiModel>,
                response: Response<VideoIdsApiModel>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        for (i in body.items) {
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
    }

    // view videos with authors (multiple ids to getvideos??)
    private fun viewVideos() {
        if (idVideos.size > 0 && idAuthors.size > 0) {
            for (i in idAuthors)
                getAuthors(i)
            for (i in idVideos)
                getVideos(i)
        }
    }

    // getting authors of videos
    private fun getAuthors(id: String) {
        val api = ApiServices.getRetrofit()
        val channel: Call<AuthorApiModel> = api.getChannel(id = id)
        channel.enqueue(object : Callback<AuthorApiModel> {
            override fun onResponse(
                call: Call<AuthorApiModel>,
                response: Response<AuthorApiModel>
            ) {
                if (response.isSuccessful) {
                    val chan = response.body()
                    if (chan != null) {
                        for (i in chan.items) {
                            authorList.add(
                                AuthorVideo(
                                    i.id,
                                    i.snippet.title,
                                    i.snippet.thumbnails.picture.url,
                                    Formatter.formatNumber(i.statistics.subscriberCount,requireContext())
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AuthorApiModel>, t: Throwable) {
                Log.i("Retrofit/IdChannel", t.stackTraceToString())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}