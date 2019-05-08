package io.github.jesterz91.pagingwithroom

import android.app.Application
import com.facebook.stetho.Stetho
import io.github.jesterz91.pagingwithroom.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        // 데이터베이스 디버깅 chrome://inspect
        Stetho.initializeWithDefaults(this)

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(vmModule))
        }
    }
}