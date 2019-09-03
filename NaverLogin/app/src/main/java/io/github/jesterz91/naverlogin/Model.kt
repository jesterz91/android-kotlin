package io.github.jesterz91.naverlogin

data class NaverLogin(
    val message: String,
    val response: NaverUser,
    val resultcode: String
)

data class NaverUser(
    val id: String,
    val name: String,
    val nickname: String,
    val email: String,
    val profile_image: String
)