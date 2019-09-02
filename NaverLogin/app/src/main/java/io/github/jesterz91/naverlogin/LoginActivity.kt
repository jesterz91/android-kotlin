package io.github.jesterz91.naverlogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import org.jetbrains.anko.*
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope, AnkoLogger {

    private val loginJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + loginJob

    private val mOAuthLoginModule = OAuthLogin.getInstance()
    private val mOAuthLoginHandler = NaverLoginHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mOAuthLoginModule.init(this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_CLIENT_NAME)

        naverLoginButton.setOAuthLoginHandler(mOAuthLoginHandler)
        naverLoginButton.setOnClickListener {
            mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler)
        }
    }

    private fun redirectActivity() {
        startActivity(intentFor<MainActivity>().clearTask().newTask())
    }

    override fun onDestroy() {
        super.onDestroy()
        loginJob.cancelChildren()
    }

    companion object {
        class NaverLoginHandler(activity: LoginActivity) : OAuthLoginHandler() {

            private val mActivity = WeakReference<LoginActivity>(activity)

            override fun run(success: Boolean) {
                val loginActivity: LoginActivity? = mActivity.get()

                if (success) {
                    loginActivity?.redirectActivity()
                } else {
                    loginActivity?.mOAuthLoginModule?.run {
                        val errorCode = getLastErrorCode(loginActivity).code
                        val errorDesc = getLastErrorDesc(loginActivity)
                        loginActivity.error { errorCode }
                        loginActivity.error { errorDesc }
                    }
                }
            }

        }
    }

}