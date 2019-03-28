package io.github.jesterz91.dustinfo.util

import io.github.jesterz91.dustinfo.model.FineDust
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FineDustApi {

    @Headers("appKey: 6b200e091d1a4d7e83fb9b4732809b33")
    @GET("weather/dust")
    fun getFineDust(
            @Query("version") versionCode : Int = 1,
            @Query("lat") lat : Double,
            @Query("lon") lon : Double
    ) : Call<FineDust>

}