package com.example.flextube.shorts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso


data class ShortsAdapter(
    private val mShorts: ArrayList<Shorts>
) : RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder>()
{
    class ShortsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var webView: WebView
    var authorLogo: ImageView
    var author: TextView
    var likes: TextView
    var dislikes: TextView
    var title: TextView

    private var context: Context

    init {
        super.itemView

        webView= itemView.findViewById(R.id.shortsScreen)
        author = itemView.findViewById(R.id.shortsAuthor)
        authorLogo = itemView.findViewById(R.id.accountShortsLogo)
        likes = itemView.findViewById(R.id.likeCount)
        dislikes = itemView.findViewById(R.id.dislikeCount)
        title = itemView.findViewById(R.id.shortsTitle)
        context = itemView.context
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true



    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsAdapter.ShortsViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.shorts_shorts_item, parent, false)
        return ShortsAdapter.ShortsViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShortsAdapter.ShortsViewHolder, position: Int) {
        val currentShorts : Shorts = mShorts[position]

        holder.webView.loadUrl("youtube.com/shorts/"+currentShorts.id_shorts)
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