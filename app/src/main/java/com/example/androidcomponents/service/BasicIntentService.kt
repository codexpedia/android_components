package com.example.androidcomponents.service

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import android.text.format.DateFormat
import android.util.Log

/*
Unlike regular service and bound service, Intent Service is doesn’t run on the UI thread.
To create an Intent Service class, the class needs to extend from the IntentService.
- It is extended from Service.
- Stops when the tasks are completed.
- It will handle all start asynchronous requests (expressed as Intents) on demand, one at a time before it destroys itself.
- The communication between intent service can be done through interface, BroadcastReceiver, or event bus libraries.
*/
class BasicIntentService : IntentService("BasicIntentService") {

    override fun onHandleIntent(intent: Intent) {
        var msg = intent.getStringExtra(PARAM_INPUT)
        msg = DateFormat.format("MM/dd/yy h:mm:ss aa", System.currentTimeMillis()).toString() + ": " + msg
        SystemClock.sleep(5000) // 5 seconds
        val resultTxt = msg + "\n" + DateFormat.format("MM/dd/yy h:mm:ss aa", System.currentTimeMillis()) + ": " + "Ok, done!\n"

        val broadcastIntent = Intent()
        broadcastIntent.action = ServiceActivity.ACTION_RESP
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT)
        broadcastIntent.putExtra(PARAM_OUTPUT, resultTxt)
        sendBroadcast(broadcastIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("IntentService", "onDestroy")
    }

    companion object {
        val PARAM_INPUT = "INPUT_MSG"
        val PARAM_OUTPUT = "OUTPUT_MSG"
    }
}