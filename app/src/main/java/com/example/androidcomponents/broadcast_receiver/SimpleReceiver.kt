package com.example.androidcomponents.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SimpleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action === "com.example.simplebroadcastreceiver.CUSTOM_INTENT1") {
            Toast.makeText(context, "Intent 1 Detected.", Toast.LENGTH_SHORT).show()
        } else if (intent.action === "com.example.simplebroadcastreceiver.CUSTOM_INTENT2") {
            Toast.makeText(context, "Intent 2 Detected.", Toast.LENGTH_SHORT).show()
        }
    }

}