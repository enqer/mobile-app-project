package com.example.flextube.playlist_item

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.shorts.ShortsApiModel
import com.example.flextube.shorts.ShortsAuthor

class ChannelsAdapter(
    private val channelItems: ArrayList<ShortsAuthor>,
    private val qItemListener: ItemClickListener,
    private var context: Context? = null
        ):
RecyclerView.Adapter<ChannelsAdapter.ChannelItemsViewHolder>(){

    class ChannelItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var context: Context
        init {
            context = itemView.context
            Log.d(ContentValues.TAG, "PlaylistItemViewHolder/PlaylistViewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelItemsViewHolder {
        Log.d(TAG, "ChannelItemsViewHolder/onCreateViewHolder")

        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_playlist_item, parent, false)
        return ChannelItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "ChannelItemsViewHolder/getItemCount")

        return channelItems.size
    }

    override fun onBindViewHolder(holder: ChannelItemsViewHolder, position: Int) {
        Log.d(TAG, "ChannelItemsViewHolder/onBindViewHolder")
    }

    interface ItemClickListener {
        fun onItemClick(channelItem: ShortsApiModel.Shorts)
    }

}

