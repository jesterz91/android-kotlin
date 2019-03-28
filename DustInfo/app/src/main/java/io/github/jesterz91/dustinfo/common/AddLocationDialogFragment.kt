package io.github.jesterz91.dustinfo.common

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import io.github.jesterz91.dustinfo.R

class AddLocationDialogFragment : DialogFragment() {

    lateinit var cityEditText : EditText

    lateinit var onAddListener: OnAddListener

    interface OnAddListener{
        fun onAdded(city: String)
    }

    fun setOnClickListener(listener: OnAddListener) {
        onAddListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_add_location, null, false)
        cityEditText  = view.findViewById(R.id.city_edit)

        return AlertDialog.Builder(context!!)
                .setTitle("위치 추가")
                .setView(view)
                .setPositiveButton("확인") { dialog, which ->
                    val city = cityEditText.text.toString()
                    onAddListener.onAdded(city)
                }
                .setNegativeButton("취소", null)
                .create()
    }

    companion object {
        fun newInstance(listener: OnAddListener) : AddLocationDialogFragment {
            return AddLocationDialogFragment().apply {
                setOnClickListener(listener)
            }
        }
    }
}


