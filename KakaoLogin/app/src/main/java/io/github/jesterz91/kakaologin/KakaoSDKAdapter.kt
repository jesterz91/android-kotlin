package io.github.jesterz91.kakaologin

import android.content.Context
import com.kakao.auth.*

class KakaoSDKAdapter(private val context: Context)  : KakaoAdapter() {

    override fun getSessionConfig() = object : ISessionConfig {

        override fun isSaveFormData(): Boolean = true

        override fun getAuthTypes(): Array<AuthType> = arrayOf(AuthType.KAKAO_LOGIN_ALL)

        override fun isSecureMode(): Boolean = false

        override fun getApprovalType(): ApprovalType = ApprovalType.INDIVIDUAL

        override fun isUsingWebviewTimer(): Boolean = false
    }

    override fun getApplicationConfig() = IApplicationConfig { context }
}