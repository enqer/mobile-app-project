package com.example.flextube.shorts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.example.flextube.video.VideoAdapter
import com.squareup.picasso.Picasso

data class ShortsAdapter(
    private val mShorts: ArrayList<ShortsItem>
) : RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder>()
{
    class ShortsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


    var authorLogo: ImageView
    var author: TextView
    var likes: TextView
    var dislikes: TextView

    private var context: Context

    init {
        super.itemView

        author = itemView.findViewById(R.id.shortsAuthor)
        authorLogo = itemView.findViewById(R.id.accountShortsLogo)
        likes = itemView.findViewById(R.id.likeCount)
        dislikes = itemView.findViewById(R.id.dislikeCount)
        context = itemView.context


    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsAdapter.ShortsViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.shorts_shorts_item, parent, false)

        return ShortsAdapter.ShortsViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShortsAdapter.ShortsViewHolder, position: Int) {
        val currentShorts : ShortsItem = mShorts[position]

        Picasso.get().load(currentShorts.authorLogo).into(holder.authorLogo)
        holder.author.text= currentShorts.author
        holder.likes.text = currentShorts.likeCount
        holder.dislikes.text = currentShorts.dislikeCount

    }

    override fun getItemCount(): Int {
        return mShorts.size
    }
}