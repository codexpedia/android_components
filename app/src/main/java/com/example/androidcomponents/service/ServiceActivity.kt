package com.example.androidcomponents.service

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.androidcomponents.R

class ServiceActivity : AppCompatActivity() {
    companion object {
        val ACTION_RESP = "com.example.androidcomponents.service.MESSAGE_PROCESSED"
    }

    internal lateinit var mboundService: BoundService
    internal var mBound = false

    // Defines callbacks for service binding, passed to bindService()
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            val binder = service as BoundService.LocalBinder
            mboundService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    private var receiver: ResponseReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
    }


    fun startSimpleService(view: View) {
        startService(Intent(baseContext, SimpleService::class.java))
    }

    fun stopSimpleService(view: View) {
        stopService(Intent(baseContext, SimpleService::class.java))
    }


    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        val intent = Intent(this, BoundService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection)
            mBound = false
        }

        unregisterReceiver(receiver)
    }

    fun boundService(view: View) {
        val randomNum = mboundService.randomNum
        Log.d("boundService", randomNum)
        Toast.makeText(this, randomNum, Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ACTION_RESP)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        receiver = ResponseReceiver()
        registerReceiver(receiver, filter)
    }

    fun intentService(view: View) {
        Log.d("intentService", "calling BasicIntentService")
        val msgIntent = Intent(this, BasicIntentService::class.java)
        msgIntent.putExtra(BasicIntentService.PARAM_INPUT, "Hello, please go to take a nap.")
        startService(msgIntent)
        Toast.makeText(applicationContext, "intent service started", Toast.LENGTH_SHORT).show()
    }

    inner class ResponseReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val text = intent.getStringExtra(BasicIntentService.PARAM_OUTPUT)
            Log.d("onReceive", text)
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }


}
