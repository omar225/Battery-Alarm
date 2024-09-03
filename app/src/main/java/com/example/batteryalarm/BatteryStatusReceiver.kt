package com.example.batteryalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager


class BatteryStatusReceiver(private val onBatteryStatusChanged: (Int) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = (level * 100) / scale.toFloat()
        onBatteryStatusChanged(batteryPct.toInt())
    }
}
