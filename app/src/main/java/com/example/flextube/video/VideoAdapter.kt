package com.example.flextube.video

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.squareup.picasso.Picasso

class VideoAdapter(
    private val mVideo: ArrayList<Video>,
    private val mItemListener: ItemClickListener,
    private var context: Context? = null
    ) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>()
{


    class VideoViewHolder(
        itemView: View

    ) : RecyclerView.ViewHolder(itemView) {
        // elementy z fragmentHome_video_item
        var videoPicture: ImageView

        var authorLogo: ImageView
        var title: TextView
        var duration: TextView
        var videoInfo: TextView

        private var context: Context

        init {
            super.itemView

            videoPicture = itemView.findViewById(R.id.video)
            duration = itemView.findViewById(R.id.duration)
            videoInfo = itemView.findViewById(R.id.videoInfo)
            title = itemView.findViewById(R.id.videoTitle)
            authorLogo = itemView.findViewById(R.id.accountLogo)

            context = itemView.context


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.home_video_item, parent, false)

        return VideoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mVideo.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem: Video = mVideo[position]

//        val linkUrl = currentItem.urlPhoto

        // create picture by url
        Picasso.get().load(currentItem.urlPhoto).into(holder.videoPicture);

        holder.title.text = currentItem.title
        holder.videoInfo.text = currentItem.authorVideo.name + " ∙ " + currentItem.viewCount + " views" + " ∙ " + currentItem.publishedDate
        holder.duration.text = currentItem.duration

        Picasso.get().load(currentItem.authorVideo.urlLogo).into(holder.authorLogo)

        holder.itemView.setOnClickListener {
            mItemListener.onItemClick(mVideo[position])
        }

    }

    public interface ItemClickListener{
        fun onItemClick(video: Video)
    }
}