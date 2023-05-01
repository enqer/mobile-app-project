package com.example.flextube.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flextube.R

class PlaylistAdapter(private val playlists: List<Playlist>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.library_playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.playlistNameTextView.text = playlist.name
        holder.playlistElementsTextView.text = "${playlist.itemCount} elements"
        Glide.with(holder.itemView.context).load(playlist.thumbnailUrl).into(holder.playlistImageView)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistImageView: ImageView = itemView.findViewById(R.id.TV_playlist_template)
        val playlistNameTextView: TextView = itemView.findViewById(R.id.TV_playlist_name)
        val playlistElementsTextView: TextView = itemView.findViewById(R.id.TV_playlist_elements)
    }
}
