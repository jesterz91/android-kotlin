package io.github.jesterz91.pagingwithroom.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.jesterz91.pagingwithroom.R
import io.github.jesterz91.pagingwithroom.db.Cheese

class CheeseAdapter : PagedListAdapter<Cheese, CheeseAdapter.CheeseViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cheese_item, parent, false)
        return CheeseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        val cheese = getItem(position)
        holder.bindView(cheese)
    }

    class CheeseViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val nameView : TextView = itemView.findViewById(R.id.name)

        var cheese : Cheese? = null

        fun bindView(cheese : Cheese?) {
            this.cheese = cheese
            nameView.text = cheese?.let {
                "${it.id}. ${it.name}"
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cheese>() {
            override fun areItemsTheSame(oldItem: Cheese, newItem: Cheese): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cheese, newItem: Cheese): Boolean =
                    oldItem == newItem
        }
    }
}