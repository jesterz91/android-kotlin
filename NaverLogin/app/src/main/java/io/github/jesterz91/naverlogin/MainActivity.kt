package io.github.jesterz91.naverlogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, AnkoLogger {

    private val mainJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + mainJob

    private val naverLoginModule = OAuthLogin.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그인 상태 확인
        if (!checkLoginState()) {
            startActivity(intentFor<LoginActivity>().clearTask().newTask())
            return
        }

        logoutButton.setOnClickListener { logout() }
        unlinkButton.setOnClickListener { unlink() }
    }

    // 네이버 로그인 상태 확인
    private fun checkLoginState(): Boolean {
        if (naverLoginModule.getState(this) === OAuthLoginState.OK) {
            val accessToken = naverLoginModule.getAccessToken(this)
            requestUserInfo(accessToken)
            return true
        }
        return false
    }

    private fun requestUserInfo(accessToken: String) = launch(Dispatchers.IO) {
        // 네이버 액세스 토큰으로 네이버 API 에 접근하여 사용자 정보를 가져옴
        val url = "https://openapi.naver.com/v1/nid/me"
        val response = naverLoginModule.requestApi(this@MainActivity, accessToken, url)
        val naverLogin = Gson().fromJson(response, NaverLogin::class.java)

        withContext(Dispatchers.Main) {
            naverLogin.response.let { naverUser ->

                Glide.with(this@MainActivity)
                    .load(naverUser.profile_image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView)

                nameTextView.text = naverUser.name
                nickNameTextView.text = naverUser.nickname
                emailTextView.text = naverUser.email
            }
        }
    }

    private fun logout() {
        naverLoginModule.logout(this)
        startActivity(intentFor<LoginActivity>().clearTask().newTask())
    }

    private fun unlink() = launch(Dispatchers.IO) {
        val isSuccessDeleteToken = naverLoginModule.logoutAndDeleteToken(this@MainActivity)

        if (isSuccessDeleteToken) {
            startActivity(intentFor<LoginActivity>().clearTask().newTask())
        } else {
            error { "ErrorCode : ${naverLoginModule.getLastErrorCode(this@MainActivity).code}" }
            error { "ErrorDesc : ${naverLoginModule.getLastErrorDesc(this@MainActivity)}" }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob.cancelChildren()
    }
}