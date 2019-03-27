package io.github.jesterz91.firebasechat


// 채팅 메시지 모델 클래스
data class ChatMessage(
        var uuid: String = "",
        val text: String = "",
        val name: String = "",
        val photoUrl: String = ""
)