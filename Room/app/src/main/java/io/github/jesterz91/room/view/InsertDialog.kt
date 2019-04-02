package io.github.jesterz91.room.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.github.jesterz91.room.R

class InsertDialog : DialogFragment() {

    lateinit var onAddListener: OnAddListener

    interface OnAddListener {
        fun onAdded(word: String)
    }

    fun setOnClickListener(listener: OnAddListener) {
        onAddListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(context).inflate(R.layout.insert_dialog, null, false)

        val wordEditText: EditText = view.findViewById(R.id.update_word_edit)

        return AlertDialog.Builder(context!!)
            .setTitle("단어 추가")
            .setView(view)
            .setPositiveButton("확인") { _, _ ->
                onAddListener.onAdded(wordEditText.text.toString())
            }
            .setNegativeButton("취소", null)
            .create()
    }
}
