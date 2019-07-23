package io.github.jesterz91.googlemapsplatform.util

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyClusterItem(
    private val position: LatLng,
    private val title: String,
    private val snippet: String
) : ClusterItem {

    override fun getPosition(): LatLng = position

    override fun getTitle(): String = title

    override fun getSnippet(): String = snippet
}