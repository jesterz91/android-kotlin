package io.github.jesterz91.firebasecloudmessage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.jetbrains.anko.warn


class MainActivity : AppCompatActivity(), AnkoLogger, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        token.setOnClickListener(this)
        subscribe.setOnClickListener(this)
        unsubscribe.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.token -> getToken()
            R.id.subscribe -> subscribeToTopic()
            R.id.unsubscribe -> unsubscribeToTopic()
        }
    }

    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        warn { "getInstanceId failed ${task.exception}" }
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    textView.text = "$token"
                    infoAndToast("new Instance ID token : $token")
                })
    }

    private fun subscribeToTopic() {
        val topic = editText.text.toString().trim()
        if (topic.isEmpty()) {
            toast("주제를 입력해주세요")
            return
        }
        // 주제 구독
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                var msg = "Subscribed to $topic"
                if (!task.isSuccessful) {
                    msg = "Failed to subscribe to weather $topic"
                }
                infoAndToast(msg)
            }
        editText.text.clear()
    }

    private fun unsubscribeToTopic() {
        val topic = editText.text.toString().trim()
        if (topic.isEmpty()) {
            toast("주제를 입력해주세요")
            return
        }
        // 주제 구독해지
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                var msg = "Unsubscribed to $topic"
                if (!task.isSuccessful) {
                    msg = "Failed to unsubscribe to weather $topic"
                }
                infoAndToast(msg)
            }
        editText.text.clear()
    }

    private fun infoAndToast(msg: String) {
        info { msg }
        toast(msg)
    }
}
