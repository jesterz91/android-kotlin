package io.github.jesterz91.kakaologin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.util.exception.KakaoException
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity(), AnkoLogger {

    private val kakaoCallback: KakaoSessionCallback by lazy {
        KakaoSessionCallback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Session.getCurrentSession().addCallback(kakaoCallback)
        Session.getCurrentSession().checkAndImplicitOpen()

        login.setOnClickListener { login.performClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(kakaoCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun redirectMainActivity() = startActivity(intentFor<MainActivity>().clearTask().newTask())

    inner class KakaoSessionCallback: ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            error { exception?.message }
        }

        override fun onSessionOpened() {
            redirectMainActivity()
        }
    }
}

