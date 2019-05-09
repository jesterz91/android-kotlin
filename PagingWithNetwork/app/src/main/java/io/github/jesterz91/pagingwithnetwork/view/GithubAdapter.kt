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

        val repo_name = itemView.findViewById<TextView>(R.id.repo_name)
        val repo_description = itemView.findViewById<TextView>(R.id.repo_description)
        val repo_language = itemView.findViewById<TextView>(R.id.repo_language)
        val repo_stars = itemView.findViewById<TextView>(R.id.repo_stars)
        val repo_forks = itemView.findViewById<TextView>(R.id.repo_forks)

        fun bindView(repo: Repo) {
            repo_name.text = repo.name
            repo_description.text = repo.description
            repo_language.text = repo.language
            repo_stars.text = repo.stargazers_count.toString()
            repo_forks.text = repo.forks.toString()
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