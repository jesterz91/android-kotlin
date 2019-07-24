package io.github.jesterz91.firebasecloudmessage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MyFirebaseMessagingService : FirebaseMessagingService(), AnkoLogger {

    override fun onNewToken(token: String?) {
        info { "new token : $token" }
        token?.let { sendToServer(it) }
    }

    private fun sendToServer(token: String) {
        info { "new token sending ... : $token" }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        info { "From: ${remoteMessage?.from}" }
        // 메시지에 데이터 페이로드가 있는지 확인
        remoteMessage?.data?.isNotEmpty()?.let {
            info { "Message data payload: ${remoteMessage.data}" }
            if (it) {
                // 10초 이상 걸리는 작업은 WorkManager 로 처리
            } else {
                // 10초 미만 작업은 바로 처리
            }
        }
        // 메시지가 알림 페이로드를 포함하는지 확인
        remoteMessage?.notification?.let {
            info { "Message Notification title : ${it.title}" }
            info { "Message Notification body : ${it.body}" }
            showNotification(it.title, it.body)
        }
    }

    private fun showNotification(title: String?, body: String?) {

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "default")
                .setContentTitle(title ?: "타이틀")
                .setContentText(body ?: "바디")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            manager.createNotificationChannel(
//                NotificationChannel("channel id", "channel name", NotificationManager.IMPORTANCE_DEFAULT))
//        }
        manager.notify(0 , builder.build())
    }
}
