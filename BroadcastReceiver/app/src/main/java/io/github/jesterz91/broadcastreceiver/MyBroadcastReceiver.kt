package io.github.jesterz91.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver: BroadcastReceiver() {

    companion object {
        const val ACTION_MY_FILTER = "ACTION_MY_FILTER"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                Toast.makeText(context, "POWER_CONNECTED", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Toast.makeText(context, "POWER_DISCONNECTED", Toast.LENGTH_SHORT).show()
            }
            ACTION_MY_FILTER -> {
                Toast.makeText(context, "My Broadcast", Toast.LENGTH_SHORT).show()
            }
        }
    }
}