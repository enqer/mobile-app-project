package com.example.flextube.playlist_item

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
import com.example.flextube.video.AuthorVideo
import com.squareup.picasso.Picasso

class PlaylistItemAdapter(
    private val playlistItems: ArrayList<PlaylistItem>,
    private val channelItems: ArrayList<AuthorVideo>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<PlaylistItemAdapter.PlaylistItemViewHolder>() {

    inner class PlaylistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val playlistItemThumbnails: ImageView
        val playlistItemTitle: TextView
        val playlistItemAuthor: TextView
        val playlistItemUser: ImageView

        init {
            playlistItemThumbnails = itemView.findViewById(R.id.IV_playlistItem_template)
            playlistItemTitle = itemView.findViewById(R.id.TV_playlistItem_title)
            playlistItemAuthor = itemView.findViewById(R.id.TV_playlistItem_author)
            playlistItemUser = itemView.findViewById(R.id.IV_playlistItem_user)

            itemView.setOnClickListener(this)

            Log.d(TAG, "PlaylistItemViewHolder/PlaylistViewHolder")
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = playlistItems[position]
                itemClickListener.onItemClick(clickedItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        Log.d(TAG, "PlaylistItemViewHolder/onCreateViewHolder")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_playlist_item, parent, false)
        return PlaylistItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "PlaylistItemViewHolder/getItemCount")

        return playlistItems.size
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        val playlistItem = playlistItems[position]

        Picasso.get().load(playlistItem.thumnailsUrl).into(holder.playlistItemThumbnails)
        holder.playlistItemTitle.text = playlistItem.title
        holder.playlistItemAuthor.text = playlistItem.channelTitle

        val channelItem = channelItems.find { it.id == playlistItem.videoOwnerChannelId }
        channelItem?.let {
            Picasso.get().load(channelItem.urlLogo).into(holder.playlistItemUser)
        }
    }

    interface ItemClickListener {
        fun onItemClick(playlistItem: PlaylistItem)
    }
}
