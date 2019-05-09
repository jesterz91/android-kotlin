package io.github.jesterz91.pagingwithnetwork

import android.app.Application
import io.github.jesterz91.pagingwithnetwork.di.apiModule
import io.github.jesterz91.pagingwithnetwork.di.vmModule
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(listOf(apiModule, vmModule))
        }
    }
}