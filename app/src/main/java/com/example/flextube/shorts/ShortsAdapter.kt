package com.example.flextube.shorts

import android.content.Context
import android.util.Log
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
import com.example.flextube.api.ApiServices
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        val useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"

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
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.settings.userAgentString = useragent
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsAdapter.ShortsViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.shorts_shorts_item, parent, false)
        return ShortsAdapter.ShortsViewHolder(v)
    }

    override fun onBindViewHolder(holder: ShortsAdapter.ShortsViewHolder, position: Int) {
        val currentShorts : Shorts = mShorts[position]

        val data_html =
            "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> " +
                    "<meta name=\"viewport\" content=\"width=100%, initial-scale=1\"> " +
                    "<link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" />" +
                    "</head> " +
                    "<style>html, body, iframe { height: 100%; width: 100%; margin: 0; padding: 0; autoplay:1;}</style>" +
                    "<body>" +
                    currentShorts.player+

             "</body> </html> "


        //holder.webView.loadUrl("$data_html?autoplay=1")
        holder.webView.loadDataWithBaseURL("https://www.youtube.com", data_html, "text/html", "UTF-8", null);
        //holder.webView.loadUrl("youtube.com/shorts/"+currentShorts.id_shorts)
        Picasso.get().load(currentShorts.channelLogoUrl).into(holder.authorLogo)
        holder.author.text= currentShorts.channelName
        holder.likes.text = currentShorts.likeCount.toString()
        holder.dislikes.text = currentShorts.dislikeCount.toString()
        holder.title.text = currentShorts.title_shorts
    }
    private fun getAuthors(id:String): String{
        var x: String = ""
        val api = ApiServices.getRetrofit()
        val channel: Call<ShortsAuthorApiModel> = api.getShortsChannel(id = id)
        channel.enqueue(object : Callback<ShortsAuthorApiModel>{
            override fun onResponse(
                call: Call<ShortsAuthorApiModel>,
                response: Response<ShortsAuthorApiModel>
            ) {
                if (response.isSuccessful){
                    val chan = response.body()
                    if (chan != null) {
                        for (i in chan.items){
                             x=i.snippet.thumbnails.picture.url
                            Log.i("logo23",x)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ShortsAuthorApiModel>, t: Throwable) {
                Log.i("Retrofit/IdChannel", t.stackTraceToString())
            }
        })
        return x
    }


    override fun getItemCount(): Int {
        return mShorts.size
    }
}