package io.github.jesterz91.googlemapsplatform

import android.app.Application
import com.google.android.libraries.places.api.Places

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(this, getString(R.string.google_maps_key))
    }
}