package com.example.flextube.playlist_item

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.video.AuthorVideo

class ChannelsAdapter(
    private val channelItems: ArrayList<AuthorVideo>
        ):
RecyclerView.Adapter<ChannelsAdapter.ChannelItemsViewHolder>() {

    class ChannelItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var context: Context

        init {
            context = itemView.context
            Log.d("PlaylistItemViewHolder/PlaylistViewHolder", "done")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelItemsViewHolder {
        Log.d("ChannelItemsViewHolder/onCreateViewHolder", "done")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_playlist_item, parent, false)
        return ChannelItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("ChannelItemsViewHolder/getItemCount", "done")

        return channelItems.size
    }

    override fun onBindViewHolder(holder: ChannelItemsViewHolder, position: Int) {
        Log.d("ChannelItemsViewHolder/onBindViewHolder", "done")
    }
}