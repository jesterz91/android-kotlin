package io.github.jesterz91.dustinfo.finedust

import io.github.jesterz91.dustinfo.model.FineDust

class FineDustContract {

    interface View {
        fun showFineDustResult(finedust: FineDust)
        fun showLoadError(message: String)
        fun loadingStart()
        fun loadingEnd()
        fun reload(lat: Double, lon: Double)
    }

    interface UserActionListener {
        fun loadFineDustData()
    }


}