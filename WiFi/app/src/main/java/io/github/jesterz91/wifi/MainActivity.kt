package io.github.jesterz91.wifi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.*
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), AnkoLogger, View.OnClickListener {

    private val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    private val permissionRequestCode = 1000

    private val wifiManager: WifiManager by lazy {
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private val wifiScanReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                info { "<WifiReceiver>" }
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)

                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 권한 요청
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
        } else {
            initButton()
        }
    }

    private fun initButton() {
        wifiOn_btn.setOnClickListener(this)
        wifiOff_btn.setOnClickListener(this)
        wifiState_btn.setOnClickListener(this)
        wifiScan_btn.setOnClickListener(this)
        wifiConnet_btn.setOnClickListener(this)
        wifiDisconnect_btn.setOnClickListener(this)
        airplaneDetect_btn.setOnClickListener(this)
        wifiMeasure_btn.setOnClickListener(this)
        connected_info_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.wifiOn_btn -> {
                info { "와이파이 켜기" }
                wifiManager.isWifiEnabled = true
            }
            R.id.wifiOff_btn -> {
                info { "와이파이 끄기" }
                wifiManager.isWifiEnabled = false
            }
            R.id.wifiState_btn -> {
                info { "와이파이 상태: ${wifiManager.isWifiEnabled}" }
            }
            R.id.wifiScan_btn -> {
                wifiManager.startScan()
                info { "와이파이 스캔 결과: ${wifiManager.scanResults}" }
            }
            R.id.wifiConnet_btn -> {
                connectWifi()
            }
            R.id.wifiDisconnect_btn -> {
                info { "와이파이 끊음" }
                wifiManager.disconnect()
            }
            R.id.airplaneDetect_btn -> {
                info { "비행기모드: " + isAirplaneModeOn(this) }
            }
            R.id.wifiMeasure_btn -> {
                //부팅된 이후로의 데이터 계산
                val mobileTx = TrafficStats.getMobileTxBytes().toInt()
                val mobileRx = TrafficStats.getMobileRxBytes().toInt()
                val wifiTx = (TrafficStats.getTotalTxBytes() - mobileTx).toInt()
                val wifiRx = (TrafficStats.getTotalRxBytes() - mobileRx).toInt()
                info { "[mobileTx]:$mobileTx, [mobileRx]:$mobileRx, [wifiTx]:$wifiTx, [wifiRx]:$wifiRx" }
            }
            R.id.connected_info_btn -> {
                connectedWifiInfoBtn()
            }
        }
    }

    private fun connectWifi() {
        val info = wifiManager.connectionInfo //get WifiInfo
        val pastId = info.networkId //get id of currently connected network
        info { "기존 와이파이: $pastId" }

        //remember id
        wifiManager.disconnect() //기존의 것은 끊고
        wifiManager.disableNetwork(pastId) //싹을 자른다
        wifiManager.removeNetwork(pastId)

        val wifiConfig = WifiConfiguration().apply {
            SSID = String.format("\"%s\"", "EMPO-3479K68")
            preSharedKey = String.format("\"%s\"", "YXDVUENL")
        }

        val netId = wifiManager.addNetwork(wifiConfig)

        wifiManager.run {
            enableNetwork(netId, true)
            reconnect()
        }

        info { "접속한 와이파이:$netId" }

        requestNetwork()
    }

    private fun requestNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder: NetworkRequest.Builder = NetworkRequest.Builder()

            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

            manager.requestNetwork(builder.build(), object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manager.bindProcessToNetwork(network)
                    } else {
                        ConnectivityManager.setProcessDefaultNetwork(network)
                    }
                    manager.unregisterNetworkCallback(this)
                }
            })
        }
    }

    private fun isAirplaneModeOn(context: Context): Boolean
        = Settings.System.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) !== 0

    private fun connectedWifiInfoBtn() {
        val info = wifiManager.connectionInfo
        info { "연결된 와이파이 정보 : $info" }
        info { "연결된 와이파이 : ${info.ssid}" }
    }

    private fun scanSuccess() {
        wifiManager.scanResults.forEach {
            info { "[SSID]: ${it.SSID} [frequency]: ${it.frequency} [BSSID]: ${it.BSSID}" }
        }
    }

    private fun scanFailure() {
        val results = wifiManager.scanResults
        info { "scanFailure(): $results" }
    }

    override fun onResume() {
        super.onResume()
        info { "onResume(), 리시버등록" }
        registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    }

    override fun onPause() {
        super.onPause()
        info { "onResume(), 리시버제거" }
        unregisterReceiver(wifiScanReceiver)
    }

    private fun checkPermission(): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequestCode -> {
                if (checkPermission()) {
                    toast("Permission Granted")
                    initButton()
                } else {
                    toast("Permission Denied")
                }
            }
        }
    }
}
