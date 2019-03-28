package io.github.jesterz91.dustinfo.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*

object GeoUtil {

    interface GeoUtilListener {
        fun onSuccess(lat: Double, lon: Double)
        fun onError(message: String)
    }

    fun getLocationFromName(context: Context, city: String, listener: GeoUtilListener) {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address> = arrayListOf()

        addresses = geocoder.getFromLocationName(city, 1)

        if (addresses.isNotEmpty()) {
            val lat = addresses[0].latitude
            val lon = addresses[0].longitude
            listener.onSuccess(lat, lon)
        } else {
            listener.onError("주소 결과가 없습니다")
        }
    }
}