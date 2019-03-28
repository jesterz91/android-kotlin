package io.github.jesterz91.dustinfo.data

import io.github.jesterz91.dustinfo.model.FineDust
import retrofit2.Callback

interface FineDustRepository {

    fun isAvailable(): Boolean

    fun getFineDustData(callback: Callback<FineDust>)
}