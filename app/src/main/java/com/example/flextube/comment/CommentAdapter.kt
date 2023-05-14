package com.example.flextube.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.R
import com.squareup.picasso.Picasso


class CommentAdapter(
    private val mComment: ArrayList<Comment>

    ) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>()
{
    class CommentViewHolder (
        itemView: View
            ) : RecyclerView.ViewHolder(itemView){
                var commentAuthorInfo: TextView
                var commmentAuthorLogo: ImageView
                var commentText: TextView
                var commentLike: TextView
                var likeLayout: ConstraintLayout
                private var context: Context

                init {
                    super.itemView
                    commentAuthorInfo = itemView.findViewById(R.id.item_comment_info)
                    commmentAuthorLogo = itemView.findViewById(R.id.item_comment_authorLogo)
                    commentText = itemView.findViewById(R.id.item_comment_text)
                    commentLike = itemView.findViewById(R.id.item_comment_like)
                    context = itemView.context
                    likeLayout = itemView.findViewById(R.id.item_like)
                }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.video_comment_item, parent, false)
        return CommentViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mComment.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem: Comment = mComment[position]
        Picasso.get().load(currentItem.authorLogo).into(holder.commmentAuthorLogo)
        holder.commentAuthorInfo.text = currentItem.author + " ∙ " + currentItem.publishedAt
        holder.commentText.text = currentItem.text
        holder.commentLike.text = currentItem.likeCount.toString()

    }
}