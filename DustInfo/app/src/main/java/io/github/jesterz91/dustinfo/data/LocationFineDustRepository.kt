package io.github.jesterz91.dustinfo.data

import io.github.jesterz91.dustinfo.model.FineDust
import io.github.jesterz91.dustinfo.util.FineDustUtil
import retrofit2.Callback

class LocationFineDustRepository(var latitude : Double? =null, val longitude: Double? =null) : FineDustRepository {


    override fun isAvailable(): Boolean {
        if (latitude != null && longitude != null) {
            return true
        }
        return false
    }

    override fun getFineDustData(callback: Callback<FineDust>) {
        FineDustUtil.fineDustApi
                .getFineDust(lat = latitude!!, lon = longitude!!)
                .enqueue(callback)
    }
}