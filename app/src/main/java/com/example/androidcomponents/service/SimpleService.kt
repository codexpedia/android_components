package com.example.androidcomponents.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

/*
A simple service is referred as unbound service or started service.
- Have an UI but it is running on the UI thread.
- Started by calling startService.
- Needs to be stopped manually by calling stopService.
*/
class SimpleService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SimpleService", "SimpleService destroyed!")
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show()
    }
}