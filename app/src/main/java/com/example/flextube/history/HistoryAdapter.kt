package com.example.flextube.history

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

class HistoryAdapter(
    private val history: ArrayList<Video>,
    private val qItemListener: HistoryAdapter.ItemClickListener,
    private var context: Context? = null
) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

        class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val historyImageView : ImageView
            val historyTextView : TextView

            private var context : Context

            init {
                historyImageView = itemView.findViewById(R.id.IV_history_template)
                historyTextView = itemView.findViewById(R.id.TV_history_name)

                context = itemView.context

                Log.d(TAG, "HistoryAdapter/HistoryViewHolder")
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        // a chuj tym torom w dupe ... Kabelki!
        Log.d(TAG, "HistoryViewHolder/onCreateViewHolder")

        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "HistoryViewHolder/getItemCount")
        return history.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = history[position]

        holder.historyTextView.text = currentItem.title
        Picasso.get().load(currentItem.urlPhoto).into(holder.historyImageView)

        holder.itemView.setOnClickListener{
            qItemListener.onItemClick(currentItem)
        }
        Log.d(TAG, "HistoryViewHolder/onBindViewHolder")
    }

    public interface ItemClickListener{
        fun onItemClick(hisotry: Video)
    }
}