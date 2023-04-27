package com.example.flextube.shorts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.squareup.picasso.Picasso

data class ShortsAdapter(
    private val mShorts: ArrayList<Shorts>
) : RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder>()
{
    class ShortsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


    var authorLogo: ImageView
    var author: TextView
    var likes: TextView
    var dislikes: TextView
    var title: TextView

    private var context: Context

    init {
        super.itemView

        author = itemView.findViewById(R.id.shortsAuthor)
        authorLogo = itemView.findViewById(R.id.accountShortsLogo)
        likes = itemView.findViewById(R.id.likeCount)
        dislikes = itemView.findViewById(R.id.dislikeCount)
        title = itemView.findViewById(R.id.shortsTitle)
        context = itemView.context


    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsAdapter.ShortsViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.shorts_shorts_item, parent, false)

        return ShortsAdapter.ShortsViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShortsAdapter.ShortsViewHolder, position: Int) {
        val currentShorts : Shorts = mShorts[position]

        Picasso.get().load(currentShorts.channelLogoUrl).into(holder.authorLogo)
        holder.author.text= currentShorts.channelName
        holder.likes.text = currentShorts.likeCount.toString()
        holder.dislikes.text = currentShorts.dislikeCount.toString()
        holder.title.text = currentShorts.title_shorts

    }

    override fun getItemCount(): Int {
        return mShorts.size
    }
}