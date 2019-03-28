package io.github.jesterz91.dustinfo.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.github.jesterz91.dustinfo.R
import io.github.jesterz91.dustinfo.db.LocationRealmObject
import io.github.jesterz91.dustinfo.finedust.FineDustContract
import io.github.jesterz91.dustinfo.finedust.FineDustFragment
import io.github.jesterz91.dustinfo.util.GeoUtil
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AnkoLogger {

    var fragmentList: MutableList<Pair<Fragment, String>> = arrayListOf()

    val REQUEST_PERMISSION = 1000

    val realm = Realm.getDefaultInstance()

    val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // 위치추가 버튼 클릭 이벤트
        fab.setOnClickListener {
            // 다이얼로그 프래그먼트 표시
            AddLocationDialogFragment.newInstance(object : AddLocationDialogFragment.OnAddListener {
                override fun onAdded(city: String) {
                    GeoUtil.getLocationFromName(this@MainActivity, city, object : GeoUtil.GeoUtilListener {
                        override fun onSuccess(lat: Double, lon: Double) {
                            saveNewCity(lat, lon, city)
                            addNewFragment(lat, lon, city)
                        }

                        override fun onError(message: String) {
                            toast(message)
                        }
                    })
                }
            }).show(supportFragmentManager, "dialog")
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        setUpViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    // 탭에 새로운 위치 추가
    fun saveNewCity(latitude: Double, longitude: Double, city: String) {
        realm.beginTransaction()
        realm.createObject<LocationRealmObject>().run {
            name = city
            lat = latitude
            lon = longitude
        }
        realm.commitTransaction()
    }

    private fun loadDbData() {
        fragmentList.clear()
        fragmentList.add(Pair(FineDustFragment(), "현재위치"))
        realm.where<LocationRealmObject>()
            .findAll()
            .forEach {
                fragmentList.add(
                    Pair(FineDustFragment.newInstance(it.lat, it.lon), it.name)
                )
            }
    }

    // DB에 저장된 값을 얻어와 탭, 뷰페이저 설정
    fun setUpViewPager() {
        loadDbData()
        viewPager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun addNewFragment(lat: Double, lon: Double, city: String) {
        fragmentList.add(Pair(FineDustFragment.newInstance(lat, lon), city))
        viewPager.adapter?.notifyDataSetChanged()
    }

    // 위치권한 얻은 후 위치정보 요청
    fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 위치권한 요청 (위치권한 요청을 거부한적이 있는 경우, 권한이 필요한 이유 표시)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                alert("위치 정보를 얻으려면 권한을 설정해야 합니다.") {
                    yesButton {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_PERMISSION
                        )
                    }
                    noButton {
                        toast("권한 설정을 취소")
                    }
                }.show()
                return

            } else {

                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION
                )
                return

            }
        }
        // 위치정보 획득
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                it?.run {
                    val view: FineDustContract.View = fragmentList[0].first as FineDustContract.View
                    view.reload(it.latitude, it.longitude)
                }
            }
    }

    // 권한 요청 결과
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation()
                } else {
                    toast("권한이 거부 되었습니다.")
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                realm.beginTransaction()
                realm.where<LocationRealmObject>().findAll().deleteAllFromRealm()
                realm.commitTransaction()
                setUpViewPager()
                return true
            }
            R.id.action_delete_current -> {
                when (tabLayout.selectedTabPosition) {
                    0 -> {
                        toast("현재 위치는 삭제할 수 없습니다.")
                    }
                    else -> {
                        realm.beginTransaction()
                        realm.where<LocationRealmObject>()
                            .findAll()[tabLayout.selectedTabPosition - 1]
                            ?.deleteFromRealm()
                        realm.commitTransaction()
                        setUpViewPager()
                    }
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_web -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://weatherplanet.co.kr/"))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    // 탭 레이아웃 설정 어댑터
    inner class MyPagerAdapter(fm: FragmentManager, val fragmentLists: List<Pair<Fragment, String>>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragmentLists[position].first
        }

        override fun getCount(): Int {
            return fragmentLists.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentLists[position].second
        }
    }

}


