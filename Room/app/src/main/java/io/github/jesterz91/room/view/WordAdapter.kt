package io.github.jesterz91.room.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.jesterz91.room.R
import io.github.jesterz91.room.repository.db.Word

class WordAdapter(val delete: (Word) -> Unit) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private var words = emptyList<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word: Word = words[position]

        holder.apply {
            wordItemView.text = word.word

            // 단어 삭제버튼 클릭시, 생성자에서 전달받은 함수를 실행 (고차함수)
            deleteWord.setOnClickListener {
                delete(word)
            }
        }
    }

    // 데이터 변경
    internal fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = words.size

    // 뷰 홀더
    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
        val deleteWord: ImageView = itemView.findViewById(R.id.delete_word)
    }
}