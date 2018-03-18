package com.example.androidcomponents.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

/*
Bound service is started by calling bindService.
- It doesn’t have an UI and it also running on the UI thread like a regular service.
- Stops when no components are binded to it anymore.
- It can have multiple components bind to it.
- It can have components such as activity bind to it through a ServiceConnection, and use it to for communication.
- It needs an custom class which extends Binder.
 */
class BoundService : Service() {
    private var mBinder: IBinder? = null
    private var mGenerator: Random? = null

    val randomNum: String
        get() = "Random Number: " + mGenerator!!.nextInt(10000)

    override fun onCreate() {
        super.onCreate()
        Log.v(LOG_TAG, "in onCreate")
        mBinder = LocalBinder()
        mGenerator = Random()
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.v(LOG_TAG, "in onBind")
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        Log.v(LOG_TAG, "in onRebind")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.v(LOG_TAG, "in onUnbind")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(LOG_TAG, "in onDestroy")
        mGenerator = null
    }

    inner class LocalBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }

    companion object {
        private val LOG_TAG = "BoundService"
    }
}