package io.github.jesterz91.pagingwithnetwork.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.jesterz91.pagingwithnetwork.R
import io.github.jesterz91.pagingwithnetwork.api.RedditPost

class RedditAdapter : PagedListAdapter<RedditPost, RedditAdapter.RedditHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reddit, parent, false)
        return RedditHolder(view)
    }

    override fun onBindViewHolder(holder: RedditHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindView(it)
        }
    }

    inner class RedditHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title = itemView.findViewById<TextView>(R.id.title)
        private val subtitle = itemView.findViewById<TextView>(R.id.subtitle)
        private val score = itemView.findViewById<TextView>(R.id.score)
        private val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)

        fun bindView(redditPost: RedditPost) {
            title.text = redditPost.title
            subtitle.text = redditPost.author
            score.text = redditPost.score.toString()
            redditPost.thumbnail?.let {
                if (it.startsWith("http")) {
                    Glide.with(itemView.context).load(it).into(thumbnail)
                } else {
                    thumbnail.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RedditPost>() {
            override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
                return oldItem == newItem
            }
        }
    }
}