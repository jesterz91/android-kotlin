package io.github.jesterz91.navigation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.jesterz91.navigation.R
import kotlinx.android.synthetic.main.fragment_flow_one.*
import kotlinx.android.synthetic.main.fragment_flow_two.*

class FlowStepFragment : Fragment() {

    private val safeArgs: FlowStepFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (safeArgs.flowStepNumber) {
            2 -> inflater.inflate(R.layout.fragment_flow_two, container, false)
            else -> inflater.inflate(R.layout.fragment_flow_one, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (safeArgs.flowStepNumber) {
            1 -> {
                next_button.setOnClickListener {
                    findNavController().navigate(R.id.action_flowStepOneFragment_to_flowStepTwoFragment)
                }
            }
            2 -> {
                finish_button.setOnClickListener {
                    findNavController().navigate(R.id.action_flowStepTwoFragment_to_homeFragment, bundleOf("flowStepNumber" to 2))
                }
            }
        }
    }
}
