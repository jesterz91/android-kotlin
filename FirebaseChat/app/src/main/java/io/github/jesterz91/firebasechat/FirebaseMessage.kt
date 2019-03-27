package io.github.jesterz91.firebasechat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class FirebaseMessage : FirebaseMessagingService(), AnkoLogger {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        debug { "onMessageReceived ID : ${remoteMessage?.messageId}" }
        debug { "onMessageReceived DATA : ${remoteMessage?.data}" }
    }
}