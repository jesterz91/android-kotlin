package io.github.jesterz91.networking

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class PhotoAdapter(var photoItems: List<Photo> = arrayListOf()) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val photo = photoItems[position]

        holder.apply {
            tvTitle.text = photo.title
            tvUrl.text = photo.url

            Glide.with(itemView.context)
                    .load(photo.thumbnailUrl)
                    .into(imageView)
        }
    }

    override fun getItemCount(): Int {
        return photoItems.size
    }

    fun updateItem(newItems: List<Photo>) {
        photoItems = newItems
        notifyDataSetChanged()
    }

    inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvUrl: TextView = itemView.findViewById(R.id.tvUrl)
    }

}
