package io.github.jesterz91.lifecycles

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LocationListener {

    private val LOCATION_PERMISSION = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 일반적인 데이터는 화면 회전시 초기화 되어버림
        chronometer.start()

        val timerViewModel = ViewModelProviders.of(this).get(TimerViewModel::class.java)

        // ViewModel 을 사용하면 화면 회전시에도 데이터가 유지
        timerViewModel.time.observe(this, Observer {
            val min = it / 60
            val sec = it % 60
            timerTextView.text = "$min : $sec "
        })

        // 위치권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
        } else {
            bindLocation()
        }
    }

    // MainActivity 를 LifecycleOwner 로 전달
    // LocationManager.BoundLocationListener 에서 Lifecycle 처리
    private fun bindLocation() {
        LocationManager.bindLocationListener(
                context = this,
                locationListener = this,
                lifecycleOwner = this
        )
    }

    // 위치권한요청 결과처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    bindLocation()
                } else {
                    Toast.makeText(this@MainActivity, "권한요청이 거부", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // LocationListener 콜백
    override fun onLocationChanged(location: Location?) {
        locationTextView.text = "위도 = ${location?.latitude}\n경도 = ${location?.longitude}"
    }

    // LocationListener 콜백
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    // LocationListener 콜백
    override fun onProviderEnabled(provider: String?) {
        Toast.makeText(this@MainActivity, "Provider 활성화: $provider", Toast.LENGTH_SHORT).show();
    }

    // LocationListener 콜백
    override fun onProviderDisabled(provider: String?) {

    }
}
