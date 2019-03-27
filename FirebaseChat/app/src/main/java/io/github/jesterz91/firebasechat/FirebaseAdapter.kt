package io.github.jesterz91.firebasechat

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

// firebase-ui-database 제공 어댑터
class FirebaseAdapter(options: FirebaseRecyclerOptions<ChatMessage>) :
    FirebaseRecyclerAdapter<ChatMessage, FirebaseAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: ChatMessage) {

        holder.apply {
            if (model.photoUrl == "") {
                profileImageView.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_account_circle_black_24dp))
            } else {
                Glide.with(itemView.context)
                    .load(model.photoUrl)
                    .into(profileImageView)
            }
            messageTextView.text = model.text
            nameTextView.text = model.name
        }
    }

    // 뷰 홀더
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: CircleImageView = itemView.findViewById(R.id.profileImageView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    }
}