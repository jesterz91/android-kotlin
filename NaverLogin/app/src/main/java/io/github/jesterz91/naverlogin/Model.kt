package io.github.jesterz91.naverlogin

data class NaverLoginResponse(
    val message: String,
    val response: Response,
    val resultcode: String
)

data class Response(
    val id: String,
    val name: String,
    val nickname: String,
    val email: String,
    val profile_image: String
)