package io.github.jesterz91.pagingwithnetwork.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.jesterz91.pagingwithnetwork.R
import io.github.jesterz91.pagingwithnetwork.api.Repo

class GithubAdapter : PagedListAdapter<Repo, GithubAdapter.GithubViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
        return GithubViewHolder(view)
    }

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        val repo = getItem(position)
        repo?.let {
            holder.bindView(it)
        }
    }

    inner class GithubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.repo_name)
        private val description = itemView.findViewById<TextView>(R.id.repo_description)
        private val language = itemView.findViewById<TextView>(R.id.repo_language)
        private val stars = itemView.findViewById<TextView>(R.id.repo_stars)
        private val forks = itemView.findViewById<TextView>(R.id.repo_forks)

        fun bindView(repo: Repo) {
            name.text = repo.name
            description.text = repo.description
            language.text = repo.language
            stars.text = repo.stargazers_count.toString()
            forks.text = repo.forks.toString()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }
}