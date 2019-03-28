package io.github.jesterz91.dustinfo.finedust

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.jesterz91.dustinfo.common.MainActivity
import io.github.jesterz91.dustinfo.R
import io.github.jesterz91.dustinfo.data.FineDustRepository
import io.github.jesterz91.dustinfo.data.LocationFineDustRepository
import io.github.jesterz91.dustinfo.model.FineDust
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class FineDustFragment : Fragment(), FineDustContract.View, AnkoLogger {

    lateinit var repository: FineDustRepository
    lateinit var presenter: FineDustPresenter

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var locationTextView: TextView
    lateinit var timeTextView: TextView
    lateinit var dustTextView: TextView


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments == null) {
            repository = LocationFineDustRepository()
            with(activity as MainActivity) {
                getLastKnownLocation()
            }
        }
        arguments?.run {
            val lat: Double = getDouble("lat")
            val lon: Double = getDouble("lon")
            repository = LocationFineDustRepository(lat, lon)
        }
        presenter = FineDustPresenter(repository, this@FineDustFragment)
        presenter.loadFineDustData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fine_dust, container, false)

        view?.run {
            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
            locationTextView = findViewById(R.id.result_location_text)
            timeTextView = findViewById(R.id.result_time_text)
            dustTextView = findViewById(R.id.result_dust_text)
        }

        // 복원
        savedInstanceState?.run {
            locationTextView.text = getString("location")
            timeTextView.text = getString("time")
            dustTextView.text = getString("dust")

        }

        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE)

        // 리프레쉬
        swipeRefreshLayout.setOnRefreshListener {
            presenter.loadFineDustData()

        }

        return view
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putString("location", locationTextView.text.toString())
            putString("time", timeTextView.text.toString())
            putString("dust", dustTextView.text.toString())
        }
    }

    override fun showFineDustResult(finedust: FineDust) {
        locationTextView.text = finedust.weather.dust[0].station.name
        timeTextView.text = finedust.weather.dust[0].timeObservation
        with(finedust.weather.dust[0].pm10) {
            dustTextView.text = "$value  ㎍/㎥,  $grade"
        }
    }

    override fun showLoadError(message: String) {
        error { "에러발생 $message"}
    }

    override fun loadingStart() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun loadingEnd() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun reload(lat: Double, lon: Double) {
        repository = LocationFineDustRepository(lat, lon)
        presenter = FineDustPresenter(repository, this@FineDustFragment)
    }

    companion object {
        fun newInstance(lat: Double, lon: Double): FineDustFragment {
            val args = Bundle().apply {
                putDouble("lat", lat)
                putDouble("lon", lon)
            }
            return FineDustFragment().apply {
                arguments = args
            }
        }
    }
}