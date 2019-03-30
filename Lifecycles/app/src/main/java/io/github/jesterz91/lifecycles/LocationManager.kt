package io.github.jesterz91.lifecycles

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class LocationManager {

    companion object {
        fun bindLocationListener(
                context: Context,
                locationListener: LocationListener,
                lifecycleOwner: LifecycleOwner): BoundLocationListener {

            return BoundLocationListener(context, locationListener, lifecycleOwner)
        }
    }

    class BoundLocationListener(
            val context: Context,
            val locationListener: LocationListener,
            lifecycleOwner: LifecycleOwner
    ) : LifecycleObserver {

        lateinit var locationManager: LocationManager

        init {
            lifecycleOwner.lifecycle.addObserver(this@BoundLocationListener)
        }

        @SuppressLint("MissingPermission")
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun addLocationListener() {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, locationListener)
            Log.d("LocationManager", "Listener 추가")

            val lastLocation: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (lastLocation != null) {
                locationListener.onLocationChanged(lastLocation)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun removeLocationListener() {
            locationManager.removeUpdates(locationListener)
            Log.d("LocationManager", "Listener 제거")
        }
    }
}