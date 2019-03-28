package io.github.jesterz91.dustinfo.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FineDustUtil {

    private val BASE_URL = "https://api.weatherplanet.co.kr/"

    // 네트워크 통신 로그 설정
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val fineDustApi: FineDustApi =
        retrofit.create(FineDustApi::class.java)


}