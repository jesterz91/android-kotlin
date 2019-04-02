package io.github.jesterz91.room

import android.app.Application
import com.facebook.stetho.Stetho

class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        /**
         *   chrome://inspect 에서 DB, SharedPreference 등의 값 확인가능
         */
        Stetho.initializeWithDefaults(this)
    }
}