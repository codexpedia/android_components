package com.example.androidcomponents.broadcast_receiver

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.example.androidcomponents.R

class BroadcastReceiverActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast_receiver)
    }

    // broadcast a custom intent.
    fun broadcastIntent1(view: View) {
        val intent = Intent(this, SimpleReceiver::class.java)
        intent.action = "com.example.simplebroadcastreceiver.CUSTOM_INTENT1"
        sendBroadcast(intent)
    }

    // broadcast a custom intent.
    fun broadcastIntent2(view: View) {
        val intent = Intent(this, SimpleReceiver::class.java)
        intent.action = "com.example.simplebroadcastreceiver.CUSTOM_INTENT2"
        sendBroadcast(intent)
    }

}
