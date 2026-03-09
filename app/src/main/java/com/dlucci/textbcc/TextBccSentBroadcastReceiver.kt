package com.dlucci.textbcc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TextBccSentBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("EIFLESent", resultCode.toString())
    }
}

class TextBccDeliveredBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("EIFLEDelivered", resultCode.toString())
    }
}