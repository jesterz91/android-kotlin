package io.github.jesterz91.lifecycles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class SeekBarFragment : Fragment() {

    lateinit var seekBar: SeekBar
    lateinit var seekBarViewModel: SeekBarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_seekbar, container, false)
        seekBar = view.findViewById(R.id.seekBar)
        seekBarViewModel = ViewModelProviders.of(activity!!).get(SeekBarViewModel::class.java)
        subscribeSeekBar()
        return view
    }

    private fun subscribeSeekBar() {
        // SeekBar 값 변경시 ViewModel 업데이트
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarViewModel.current.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // ViewModel 값 변경시 SeekBar 업데이트
        seekBarViewModel.current.observe(activity!!, Observer {
            if (it != null) {
                seekBar.progress = it
            }
        })
    }
}