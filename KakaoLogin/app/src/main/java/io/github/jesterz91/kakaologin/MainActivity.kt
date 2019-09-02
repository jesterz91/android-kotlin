package io.github.jesterz91.kakaologin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestMe()

        logoutButton.setOnClickListener { logout() }
        unlinkButton.setOnClickListener { unlink() }
    }

    private fun requestMe() {
        val keys = listOf("properties.nickname", "kakao_account.email", "properties.profile_image", "properties.thumbnail_image")
        UserManagement.getInstance().me(keys, object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response) {

                Glide.with(this@MainActivity)
                    .load(result.profileImagePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView)

                nameTextView.text = result.id.toString()
                nickNameTextView.text = result.nickname
                emailTextView.text = result.kakaoAccount.toString()
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                // 로그인화면으로 이동
                startActivity(intentFor<LoginActivity>().clearTask().newTask())
            }
        })
    }

    private fun logout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                startActivity(intentFor<LoginActivity>().clearTask().newTask())
            }
        })
    }

    private fun unlink() {
        alert("카카오 로그인 연동해제") {
            yesButton {
                UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
                    override fun onFailure(errorResult: ErrorResult?) {
                        error { errorResult?.errorMessage }
                    }

                    override fun onSessionClosed(errorResult: ErrorResult) {
                        startActivity(intentFor<LoginActivity>().clearTask().newTask())
                    }

                    override fun onNotSignedUp() {
                        startActivity(intentFor<LoginActivity>().clearTask().newTask())
                    }

                    override fun onSuccess(userId: Long?) {
                        startActivity(intentFor<LoginActivity>().clearTask().newTask())
                    }
                })
            }
            noButton {
                toast("취소")
            }
        }.show()
    }
}
