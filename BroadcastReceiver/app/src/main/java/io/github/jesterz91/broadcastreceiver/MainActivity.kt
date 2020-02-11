package io.github.jesterz91.broadcastreceiver

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val myBroadcastReceiver by lazy { MyBroadcastReceiver() }

    private val intentFilter: IntentFilter by lazy {
        IntentFilter().apply {
            addAction(MyBroadcastReceiver.ACTION_MY_FILTER)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sendButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent().apply {
            action = MyBroadcastReceiver.ACTION_MY_FILTER
        }
        sendBroadcast(intent)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(myBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myBroadcastReceiver)
    }
}
