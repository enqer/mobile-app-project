package com.example.flextube.shorts

import android.content.Context
import android.util.DisplayMetrics
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
        webView.settings.mediaPlaybackRequiresUserGesture=false
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.loadsImagesAutomatically = true
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);



    }
}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsAdapter.ShortsViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.shorts_shorts_item, parent, false)
        return ShortsAdapter.ShortsViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShortsAdapter.ShortsViewHolder, position: Int) {
        val currentShorts : Shorts = mShorts[position]

        holder.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress >= 100) {
                    holder.webView.evaluateJavascript("document.getElementsByTagName('video')[0].play();", null)
                }
            }
        }

        val displaymetrics = DisplayMetrics()
        //windowManager.defaultDisplay.getMetrics(displaymetrics)
        val height = displaymetrics.heightPixels
        val width = displaymetrics.widthPixels
        val VIDEO_URL = "https://www.youtube.com/watch?v=IcqKOeK8ZcU"
        val data_html =
            "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> " +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> " +
                    "<link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" />" +
                    "</head> " +
                    "<body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> " + currentShorts.player+
                    "<iframe frameborder=0 allowfullscreen width=100% height=100% src=link  frameborder=0 allowfullscreen></iframe>" +
                    "</body> </html> "
        holder.webView.loadUrl("$data_html?autoplay=1")
        holder.webView.loadDataWithBaseURL("https://www.youtube.com", data_html, "text/html", "UTF-8", null);
        //holder.webView.loadUrl("youtube.com/shorts/"+currentShorts.id_shorts)
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