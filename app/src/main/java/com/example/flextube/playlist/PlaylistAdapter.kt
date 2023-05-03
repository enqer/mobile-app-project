package com.example.flextube.playlist

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.video.VideoAdapter
import com.squareup.picasso.Picasso
import retrofit2.http.Tag

class PlaylistAdapter(
    private val playlists: ArrayList<Playlist>,
    private val mItemListener: PlaylistAdapter.ItemClickListener,
    private var context: Context? = null
    ) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {



    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistImageView : ImageView
        val playlistNameTextView: TextView
        val playlistElementsTextView : TextView

        private var context: Context

        init {
            playlistImageView = itemView.findViewById(R.id.IV_playlist_template)
            playlistNameTextView = itemView.findViewById(R.id.TV_playlist_name)
            playlistElementsTextView = itemView.findViewById(R.id.TV_playlist_elements)

            context = itemView.context

            Log.d(TAG, "PlaylistAdapter/PlaylistViewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        Log.d(TAG, "PlaylistAdapter/onCreateViewHolder")

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.library_playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "PlaylistAdapter/getItemCount")

        return playlists.size
    }


    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        holder.playlistNameTextView.text = playlist.title
        holder.playlistElementsTextView.text = "${playlist.itemCount} elements"
        Picasso.get().load(playlist.thumbnailUrl).into(holder.playlistImageView)

        holder.itemView.setOnClickListener{
            mItemListener.onItemClick(playlists[position])
        }

        Log.d(TAG, "PlaylistAdapter/onBindViewHolder")
    }

    public interface ItemClickListener{
        fun onItemClick(playlist: Playlist)
    }
}