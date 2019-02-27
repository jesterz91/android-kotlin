package io.github.jesterz91.networking

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {

    @GET("photos")
    fun getPhotos(@Query("albumId") id: Int): Single<List<Photo>>

}