package io.github.jesterz91.googlemapsplatform.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import io.github.jesterz91.googlemapsplatform.R
import io.github.jesterz91.googlemapsplatform.util.AUTOCOMPLETE_REQUEST_CODE
import io.github.jesterz91.googlemapsplatform.util.PERMISSION_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_places.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class PlacesActivity : AppCompatActivity(), AnkoLogger, View.OnClickListener {

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val placesClient: PlacesClient by lazy {
        Places.createClient(this)
    }

    private var placesId: String? = null

    // 장소요청 후 사용할 필드 : https://developers.google.com/places/android-sdk/place-details?hl=ko
    private val placeFields =
        listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.PHOTO_METADATAS
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        autocompletePlace.setOnClickListener(this)
        findCurrentPlace.setOnClickListener(this)
        fetchPlace.setOnClickListener(this)
    }

    @SuppressLint("MissingPermission")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.autocompletePlace -> searchPlace() // 검색으로 장소얻기
            R.id.findCurrentPlace -> findPlace() // 현재위치 주변 장소얻기
            R.id.fetchPlace -> fetchPlace() // placeId 로 장소얻기
        }
    }

    // 검색으로 장소얻기
    private fun searchPlace() {
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    // 현재위치 주변 장소얻기
    private fun findPlace() {
        if (!checkPermission()) { // 권한 요청
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
            return
        }
        val request = FindCurrentPlaceRequest.newInstance(placeFields)
        val placeResponse = placesClient.findCurrentPlace(request)

        placeResponse.addOnCompleteListener {
            if (it.isSuccessful) {
                val response = it.result
                response?.placeLikelihoods?.forEach {
                    info { "장소명 : ${it.place.name} / 주소 : ${it.place.address} / 위치 : ${it.place.latLng} / 확률 : ${it.likelihood}" }
                }
            } else {
                val exception = it.exception
                if (exception is ApiException) {
                    error { "Place not found: ${exception.statusCode}" }
                }
            }
        }
    }

    // placeId 로 장소얻기
    private fun fetchPlace() {
        val request = FetchPlaceRequest.newInstance(placesId ?: "ChIJyZXqmYmjfDURyoz-CQ8bYWQ", placeFields)

        placesClient.fetchPlace(request).run {
            addOnSuccessListener {
                info { "${it.place}" }
                // 사진데이터 요청
                val photoMetadata = it.place.photoMetadatas?.get(0)
                fetchPhoto(photoMetadata)
            }
            addOnFailureListener {
                if (it is ApiException) {
                    error { "Place not found: ${it.message}" }
                }
            }
        }
    }

    private fun fetchPhoto(photoMetadata: PhotoMetadata?) {
        photoMetadata?.let { meta ->
            val attributions = meta.attributions
            info { "attributions : $attributions" }

            val photoRequest = FetchPhotoRequest.builder(meta)
                .setMaxWidth(500) // Optional.
                .setMaxHeight(300) // Optional.
                .build()

            placesClient.fetchPhoto(photoRequest).run {
                addOnSuccessListener { fetchPhotoResponse ->
                    val bitmap = fetchPhotoResponse.bitmap
                    imageView.setImageBitmap(bitmap)
                }

                addOnFailureListener { exception ->
                    if (exception is ApiException) error { "Place not found: ${exception.message}" }
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AUTOCOMPLETE_REQUEST_CODE -> { // 장소검색 후 선택 결과
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        placesId = place.id

                        info { "$place" }
                        place.photoMetadatas?.let {
                            fetchPhoto(it[0])
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        val status = Autocomplete.getStatusFromIntent(data!!)
                        toast("${status.statusMessage}")
                    }
                    Activity.RESULT_CANCELED -> {
                        toast("검색 취소")
                    }
                }
            }
        }
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
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (checkPermission()) {
                    findPlace()
                } else {
                    toast("권한이 거부되었습니다.")
                }
            }
        }
    }

}
