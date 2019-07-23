package io.github.jesterz91.googlemapsplatform.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import io.github.jesterz91.googlemapsplatform.R
import io.github.jesterz91.googlemapsplatform.util.MyClusterItem
import org.jetbrains.anko.toast


class ClusterActivity : AppCompatActivity(), OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<MyClusterItem> {

    private lateinit var map: GoogleMap

    private lateinit var clusterManager: ClusterManager<MyClusterItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cluster)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // 클러스터링 설정
        clusterManager = ClusterManager(this@ClusterActivity, map)
        clusterManager.setOnClusterItemClickListener(this)
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.514655, 127.033048), 10f))

        addItems()
    }

    private fun addItems() {
        var lat = 37.514655
        var lng = 127.033048

        // lat,lng 근처에 클러스터 아이템 추가
        for (i in 0..9) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            val offsetItem = MyClusterItem(LatLng(lat, lng), "아이템 $i", "인포")
            clusterManager.addItem(offsetItem)
        }
    }

    override fun onClusterItemClick(myClusterItem: MyClusterItem): Boolean {
        toast("${myClusterItem.title} 클릭")
        return true
    }
}
