package io.github.jesterz91.naverlogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import java.lang.ref.WeakReference

class LoginActivity : AppCompatActivity(), AnkoLogger {

    private val naverLoginModule = OAuthLogin.getInstance()
    private val naverLoginHandler = NaverLoginHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        naverLoginModule.init(
            this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_CLIENT_NAME
        )

        naverLoginButton.setOAuthLoginHandler(naverLoginHandler)

        naverLoginButton.setOnClickListener {
            naverLoginModule.startOauthLoginActivity(this, naverLoginHandler)
        }
    }

    private fun redirectMainActivity() = startActivity(intentFor<MainActivity>().clearTask().newTask())

    companion object {
        class NaverLoginHandler(activity: LoginActivity) : OAuthLoginHandler() {

            private val mActivity = WeakReference<LoginActivity>(activity)

            override fun run(success: Boolean) {
                val loginActivity: LoginActivity? = mActivity.get()
                // 로그인 성공여부
                if (success) {
                    loginActivity?.redirectMainActivity()
                } else {
                    loginActivity?.naverLoginModule?.run {
                        // 에러 로그
                        loginActivity.error { "ErrorCode : ${getLastErrorCode(loginActivity).code}" }
                        loginActivity.error { "ErrorDesc : ${getLastErrorDesc(loginActivity)}" }
                    }
                }
            }

        }
    }

}