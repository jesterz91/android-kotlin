package io.github.jesterz91.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager


class MainActivity : AppCompatActivity(), SensorEventListener {

    private val TAG = "MainActivity"
    // 지연된 초기화를 사용하여, 변수를 처음 사용할 때 SensorManager 객체를 얻음
    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private lateinit var tiltView: TiltView

    override fun onCreate(savedInstanceState: Bundle?) {
        // 화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // 화면을 가로모드로 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        tiltView = TiltView(this)
        setContentView(tiltView)
    }

    // 센서 등록
    override fun onResume() {
        super.onResume()
        // 가속도 센서를 사용하여, 화면 방향이 전환될 때 적합할 정도로 센서값을 얻음.
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    // 센서 해제
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // 센서 정밀도가 변경되면 호출
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    // 센서값이 변경되면 호출
    override fun onSensorChanged(event: SensorEvent?) {
        // event.values[0] : x축 값 , 위로 기울이면 -10~0, 아래로 기울이면 0~10
        // event.values[1] : y축 값 , 왼쪽으로 기울이면 -10~0, 오른쪽으로 기울이면 0~10
        // event.values[2] : z축 값
        event?.let {
            Log.d(TAG, "onSensorChanged : x : " +
                    "${event.values[0]} , y : ${event.values[1]} , z : ${event.values[2]}")
            tiltView.onSensorEvent(event)
        }
    }
}
