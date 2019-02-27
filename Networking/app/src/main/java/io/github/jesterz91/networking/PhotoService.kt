package io.github.jesterz91.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PhotoService {

    companion object {

        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        private var instance: Retrofit? = null

        @Synchronized
        fun getInstance(): Retrofit {
            if (instance == null) {

                val interceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                val client = OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build()

                return Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }

            return instance!!
        }
    }
}