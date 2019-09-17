package io.github.jesterz91.navigation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import io.github.jesterz91.navigation.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        navigate_destination_button.setOnClickListener {
            findNavController().navigate(R.id.flowStepOneFragment, null, options)
        }

        navigate_action_button.setOnClickListener {
            val bundle = bundleOf("flowStepNumber" to 1)
            findNavController().navigate(R.id.action_homeFragment_to_flowStepOneFragment, bundle)
        }
    }
}