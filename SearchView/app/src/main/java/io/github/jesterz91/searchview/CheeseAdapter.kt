package io.github.jesterz91.searchview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

class CheeseAdapter(private val unfiltered: List<String>) : RecyclerView.Adapter<CheeseViewHolder>(), Filterable {

    private var filtered: List<String> = unfiltered

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cheese, parent, false)
        return CheeseViewHolder(view)
    }

    override fun getItemCount(): Int = filtered.size

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) = holder.bindItem(filtered[position])

    override fun getFilter(): Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charString = constraint.toString()

            filtered = when (charString.isNotBlank()) {
                true -> {
                    unfiltered.filter { it.contains(charString) }
                }
                false -> {
                    unfiltered
                }
            }
            return FilterResults().apply { values = filtered }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filtered = results?.values as List<String>
            notifyDataSetChanged()
        }
    }
}