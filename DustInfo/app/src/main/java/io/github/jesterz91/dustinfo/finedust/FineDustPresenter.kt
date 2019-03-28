package io.github.jesterz91.dustinfo.finedust

import io.github.jesterz91.dustinfo.data.FineDustRepository
import io.github.jesterz91.dustinfo.model.FineDust
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FineDustPresenter(val repository: FineDustRepository, val view: FineDustContract.View) : FineDustContract.UserActionListener {

    override fun loadFineDustData() {
        if (repository.isAvailable()) {

            view.loadingStart()

            repository.getFineDustData(object : Callback<FineDust> {
                override fun onResponse(call: Call<FineDust>, response: Response<FineDust>) {
                    if (response.isSuccessful) {
                        view.showFineDustResult(response.body()!!)
                        view.loadingEnd()
                    }
                }

                override fun onFailure(call: Call<FineDust>, t: Throwable) {
                    view.showLoadError(t.localizedMessage)
                    view.loadingEnd()
                }
            })
        }
    }

}