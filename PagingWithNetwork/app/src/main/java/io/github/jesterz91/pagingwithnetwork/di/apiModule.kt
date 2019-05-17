package io.github.jesterz91.pagingwithnetwork.di

import io.github.jesterz91.pagingwithnetwork.api.GithubApi
import io.github.jesterz91.pagingwithnetwork.api.RedditApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val GITHUB_BASE_URL = "https://api.github.com/"
const val REDDIT_BASE_URL = "https://www.reddit.com/"

val apiModule = module {

    // Github APi
    single {
        Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }

    // Reddit APi
    single {
        Retrofit.Builder()
            .baseUrl(REDDIT_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RedditApi::class.java)
    }

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(get() as HttpLoggingInterceptor).build()
    }

    // HttpLoggingInterceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }
}