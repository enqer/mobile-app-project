package com.example.flextube.ui.library

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.flextube.R
import com.example.flextube.api.ApiServices
import com.example.flextube.database.DatabaseHelper
import com.example.flextube.databinding.FragmentLibraryBinding
import com.example.flextube.playlist.Playlist
import com.example.flextube.playlist.PlaylistAdapter
import com.example.flextube.playlist.PlaylistApiModel
import com.example.flextube.playlist_item.PlaylistActivity
import com.example.flextube.settings.SettingsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Start LibraryFragment
class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val playlistlist: ArrayList<Playlist> = ArrayList()
    private val DARK_MODE = "darkMode"

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root

        // Here add functions or code to execute

    }

    // Better option to add functions or code to execute
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG,"LibraryFragment/onViewCreated -> Start thinking, apes together strong")

//        val imageView = view.findViewById<ImageView>(R.id.person_icon)
//
//        // Start new activity (SettingsActivity)
//        imageView.setOnClickListener {
//            Log.d(TAG,"LibraryFragment/onViewCreated/imageView.setOnClickListener -> Start new activity")
//
//            val intent = Intent(activity, SettingsActivity::class.java)
//            startActivity(intent)
//        }

        readDatabase()
        getPlaylist()

    }

    private fun readDatabase(){
        val dbHelper = context?.let { DatabaseHelper(it) }
        val db = dbHelper?.writableDatabase

        val selectQuery = "SELECT video_id, urlPhotoValue, authorVideo, titleValue FROM my_table8"

        val cursor = db?.rawQuery(selectQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (cursor != null) {
                    do {
                        val idIndex = cursor.getColumnIndex("video_id")
                        val idValue = cursor.getString(idIndex)

                        val urlPhotoValueIndex = cursor.getColumnIndex("urlPhotoValue")
                        val urlPhotoValue = cursor.getString(urlPhotoValueIndex)

                        val titleValueIndex = cursor.getColumnIndex("titleValue")
                        val titleValue = cursor.getString(titleValueIndex)

                        val authorVideoIndex = cursor.getColumnIndex("authorVideo")
                        val authorVideoValue = cursor.getString(authorVideoIndex)
                        Log.d(
                            "MyApp",
                            "Pobrana wartość: $idValue, $urlPhotoValue, $titleValue, $authorVideoValue"
                        )
                    } while (cursor.moveToNext())
                }
            }
        }

        if (cursor != null) {
            cursor.close()
        }
//        if (dbHelper != null) {
//            dbHelper.close()
//        }


    }

    // Search available playlist on channel using channelId then show them
    private fun getPlaylist() {
        // Variables used in SQLite stuff
        //val dbHelper = context?.let { DatabaseHelper(it) }
        //val db = dbHelper?.writableDatabase

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
                            mAdapter = PlaylistAdapter(playlistlist, object : PlaylistAdapter.ItemClickListener{
                                override fun onItemClick(playlist: Playlist) {
                                    Log.d(TAG, "LibraryFragment/getPlaylist/onItemClick -> Item clicked")

//                                    // Save and store data in SQLite
//                                    val dataValue = playlist.id
//
//                                    val insertQuery = "INSERT INTO my_table8 (data) VALUES ('$dataValue')"
//                                    if (db != null) {
//                                        db.execSQL(insertQuery)
//                                    }
//
//                                    val selectQuery = "SELECT data FROM my_table8"
//                                    val cursor = db?.rawQuery(selectQuery, null)
//
//                                    if (cursor != null) {
//                                        if (cursor.moveToFirst()) {
//                                            if (cursor != null) {
//                                                do {
//                                                    val columnIndex = cursor.getColumnIndex("data")
//                                                    val dataValue = cursor.getString(columnIndex)
//                                                    Log.d("MyApp", "Pobrana wartość: $dataValue")
//                                                } while (cursor.moveToNext())
//                                            }
//                                        }
//                                    }
//
//                                    // CODE REQUIRED TO RESET ALL ITEMS IN DATABASE
//                                    // UNCOMMENT THAT LINES AND CLICK THE BUTTON
//                                    // THEN SHUT DOWN YOUR APP AND COMMENT THAT LINES AGAIN
//
////                                    val deleteQuery = "DELETE FROM my_table"
////                                    if (db != null) {
////                                        db.execSQL(deleteQuery)
////                                    }
//
//                                    // END OF RESTARTING DATABASE CODE
//
//                                    if (cursor != null) {
//                                        cursor.close()
//                                    }
//                                    // End of SQLite

                                    // Starts new activity
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