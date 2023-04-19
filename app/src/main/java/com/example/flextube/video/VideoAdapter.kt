package com.example.flextube.video

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.squareup.picasso.Picasso

class VideoAdapter
    (
    private val mVideo: ArrayList<Video>,
//    val mItemListener: ItemClickListener
    ) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>()
{
        companion object{
//            lateinit var mVideo: ArrayList<Video>
//            lateinit var mItemListener: OnItemClickListener
            lateinit var context: Context
        }

    class VideoViewHolder(
        itemView: View

    ) : RecyclerView.ViewHolder(itemView) {
        // elementy z fragmentHome_video_item
        var videoPicture: ImageView
        lateinit var creatorName: TextView
        lateinit var creatorLogo: ImageView
        var title: TextView

        lateinit var videoInfo: TextView

        private var context: Context

        init {
            super.itemView

            videoPicture = itemView.findViewById(R.id.video)
            videoInfo = itemView.findViewById(R.id.videoInfo)
            title = itemView.findViewById(R.id.videoTitle)

            context = itemView.context

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.home_video_item, parent, false)
        val vvh: VideoViewHolder = VideoViewHolder(v)

        return vvh
    }

    override fun getItemCount(): Int {
        return mVideo.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem: Video = mVideo[position]

        val linkUrl = currentItem.link

            Picasso.get().load(linkUrl).into(holder.videoPicture);
        holder.title.text = currentItem.title
        holder.videoInfo.text = currentItem.creatorName + currentItem.views + currentItem.uploadDate
        // sets a data
        // TODO
    }

    public interface ItemClickListener{
        fun onItemClick(video: Video)
    }
}