package io.github.jesterz91.kakaologin

import android.app.Application
import android.content.pm.PackageManager
import android.util.Base64
import com.kakao.auth.KakaoSDK
import com.kakao.util.helper.Utility
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MyApplication : Application(), AnkoLogger {

    override fun onCreate() {
        super.onCreate()
        KakaoSDK.init(KakaoSDKAdapter(this))
        //getKeyHash()
    }

    private fun getKeyHash() {
        val packageInfo = Utility.getPackageInfo(this, PackageManager.GET_SIGNATURES)

        packageInfo.signatures.forEach { signature ->
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                info { "key_hash=${Base64.encodeToString(md.digest(), Base64.DEFAULT)}" }
            } catch (e: NoSuchAlgorithmException) {
                error { "Unable to get MessageDigest. signature=$signature" }
                error { e.message }
            }
        }
    }
}