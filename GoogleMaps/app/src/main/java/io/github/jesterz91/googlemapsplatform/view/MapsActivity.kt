package io.github.jesterz91.googlemapsplatform.view

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.github.jesterz91.googlemapsplatform.R
import io.github.jesterz91.googlemapsplatform.util.PATTERN_DASH_LENGTH_PX
import io.github.jesterz91.googlemapsplatform.util.PATTERN_GAP_LENGTH_PX
import io.github.jesterz91.googlemapsplatform.util.POLYGON_STROKE_WIDTH_PX
import io.github.jesterz91.googlemapsplatform.util.POLYLINE_STROKE_WIDTH_PX
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class MapsActivity : AppCompatActivity(), AnkoLogger,
    OnMapReadyCallback,
    GoogleMap.OnPolylineClickListener,
    GoogleMap.OnPolygonClickListener,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnInfoWindowClickListener {

    private lateinit var map: GoogleMap

    private val dot = Dot()
    private val dash = Dash(PATTERN_DASH_LENGTH_PX)
    private val gap = Gap(PATTERN_GAP_LENGTH_PX)

    private val PATTERN_POLYGON_ALPHA = listOf(gap, dash)
    private val PATTERN_POLYGON_BETA = listOf(dot, gap, dash, gap)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.515345, 127.035441), 13f))
            setOnPolylineClickListener(this@MapsActivity)
            setOnPolygonClickListener(this@MapsActivity)
            setOnMarkerClickListener(this@MapsActivity)
            setOnMarkerDragListener(this@MapsActivity)
            setOnInfoWindowClickListener(this@MapsActivity)
        }
        addMarker() // 마커추가
        addPolyline() // 폴리라인 추가
        addPolygon() // 폴리곤 추가
        addCircle() // 서클 추가
    }

    private fun addMarker() {
        val yeoksam = LatLng(37.500619, 127.036441)
        val seolleung = LatLng(37.504283, 127.048186)
        val gangnam = LatLng(37.497909, 127.027589)

        val marker_yeoksam = map.addMarker(
            MarkerOptions()
                .position(yeoksam)
                .title("역삼역")
                .draggable(true)
        )

        val marker_seolleung = map.addMarker(
            MarkerOptions()
                .position(seolleung)
                .title("선릉역")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )

        val marker_gangnam = map.addMarker(
            MarkerOptions()
                .position(gangnam)
                .title("강남역")
                .snippet("환승역 2호선/신분당선")
                .alpha(0.7f)
        )

        marker_yeoksam.tag = "역삼역"
        marker_seolleung.tag = "선릉역"
        marker_gangnam.tag = "강남역"
        marker_gangnam.showInfoWindow()
    }

    // 지도에 선을 그릴 폴리 라인을 추가
    private fun addPolyline() {
        val dosan = map.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(37.519804, 127.028311),
                    LatLng(37.520417, 127.030606),
                    LatLng(37.520858, 127.031849),
                    LatLng(37.521102, 127.032742),
                    LatLng(37.521810, 127.034605)
                )
        )
        val eonju = map.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(37.515070, 127.035294),
                    LatLng(37.514017, 127.035396),
                    LatLng(37.512479, 127.035524),
                    LatLng(37.511143, 127.036085),
                    LatLng(37.510030, 127.037182),
                    LatLng(37.508836, 127.038483)
                )
        )
        stylePolyline(dosan.apply { tag = "도산대로" })
        stylePolyline(eonju.apply { tag = "언주로" })
    }

    //지도상의 영역을 나타내는 폴리곤 추가하기
    private fun addPolygon() {
        val nonhyeon: Polygon = map.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(37.511066, 127.021465),
                    LatLng(37.513927, 127.030762),
                    LatLng(37.507317, 127.033901),
                    LatLng(37.504445, 127.024507)
                )
        )

        val chungdam: Polygon = map.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(37.524058, 127.047448),
                    LatLng(37.524859, 127.054341),
                    LatLng(37.520050, 127.056993),
                    LatLng(37.518748, 127.050190)
                )
        )

        stylePolygon(nonhyeon.apply { tag = "논현동" })
        stylePolygon(chungdam.apply { tag = "청담동" })
    }

    private fun stylePolyline(polyline: Polyline) {
        when (polyline.tag) {
            "도산대로" -> {
                polyline.apply {
                    jointType = JointType.ROUND
                    endCap = RoundCap()
                    width = POLYLINE_STROKE_WIDTH_PX
                    color = Color.RED
                }
            }
            "언주로" -> {
                polyline.apply {
                    jointType = JointType.DEFAULT
                    endCap = RoundCap()
                    width = POLYLINE_STROKE_WIDTH_PX
                    color = Color.BLUE
                }
            }
        }
    }

    private fun stylePolygon(polygon: Polygon) {
        when (polygon.tag) {
            "논현동" -> {
                polygon.apply {
                    fillColor = 0xf88000000.toInt()
                    strokeColor = Color.GREEN
                    strokeWidth =
                        POLYGON_STROKE_WIDTH_PX
                    strokePattern = PATTERN_POLYGON_ALPHA
                }
            }
            "청담동" -> {
                polygon.apply {
                    fillColor = 0xf88ff0000.toInt()
                    strokeColor = Color.BLUE
                    strokeWidth =
                        POLYGON_STROKE_WIDTH_PX
                    strokePattern = PATTERN_POLYGON_BETA
                }
            }
        }
    }

    private fun addCircle() {
        val circleOptions = CircleOptions()
            .center(LatLng(37.502648, 127.042763)) // 중심위치
            .radius(100.0) // 미터단위
            .strokeWidth(POLYGON_STROKE_WIDTH_PX)
            .strokeColor(Color.MAGENTA)
            .fillColor(Color.argb(128, 0, 0, 255))
            .clickable(true)

        map.addCircle(circleOptions).tag = "테헤란로"

        map.setOnCircleClickListener {
            it.strokeColor = Color.YELLOW
            toast("${it.tag}")
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        toast("마커 : ${marker?.tag}")
        return false
    }

    override fun onInfoWindowClick(marker: Marker?) {
        toast("${marker?.snippet}")
    }

    override fun onPolylineClick(polyline: Polyline?) {
        when (polyline?.tag) {
            "도산대로" -> toast("도산대로")
            "언주로" -> toast("언주로")
        }
    }

    override fun onPolygonClick(polygon: Polygon?) {
        when (polygon?.tag) {
            "논현동" -> toast("논현동")
            "청담동" -> toast("청담동")
        }
    }

    override fun onMarkerDragStart(marker: Marker?) {
        info { "${marker?.tag} : ${marker?.position} 드래그 시작" }
    }

    override fun onMarkerDrag(marker: Marker?) {
        info { "${marker?.tag} : ${marker?.position} 드래그" }
    }

    override fun onMarkerDragEnd(marker: Marker?) {
        info { "${marker?.tag} : ${marker?.position} 드래그 종료" }
    }
}
