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
import com.example.flextube.video.Video
import com.squareup.picasso.Picasso

class PlaylistItemAdapter (
    private val playlistItems: ArrayList<PlaylistItem>,
    //private val mVideo: ArrayList<Video>,
    private val mItemListener: PlaylistItemAdapter.ItemClickListener,
    private var context: Context? = null
        ):
    RecyclerView.Adapter<PlaylistItemAdapter.PlaylistItemViewHolder>() {


    class PlaylistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistItemThumnails: ImageView
        val playlistItemTitle: TextView
        val playlistItemAuthor: TextView
        val playlistItemUser: ImageView

        private var context: Context

        init {
            playlistItemThumnails = itemView.findViewById(R.id.IV_playlistItem_template)
            playlistItemTitle = itemView.findViewById(R.id.TV_playlistItem_title)
            playlistItemAuthor = itemView.findViewById(R.id.TV_playlistItem_author)
            playlistItemUser = itemView.findViewById(R.id.IV_playlistItem_user)

            context = itemView.context

            Log.d(TAG, "PlaylistItemViewHolder/PlaylistViewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        Log.d(TAG, "PlaylistItemViewHolder/onCreateViewHolder")

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_playlist_item, parent, false)
        return PlaylistItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "PlaylistItemViewHolder/getItemCount")

        return playlistItems.size
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        val playlistItem = playlistItems[position]

        Picasso.get().load(playlistItem.thumnailsUrl).into(holder.playlistItemThumnails)
        holder.playlistItemTitle.text = playlistItem.title
        holder.playlistItemAuthor.text = playlistItem.channelTitle
        Picasso.get().load(playlistItem.videoOwnerChannelId).into(holder.playlistItemUser)
        //Picasso.get().load(playlistItem.authorVideo.urlLogo).into(holder.playlistItemUser)

    }

    interface ItemClickListener {
        fun onItemClick(playlistItem: PlaylistItem)
    }

}