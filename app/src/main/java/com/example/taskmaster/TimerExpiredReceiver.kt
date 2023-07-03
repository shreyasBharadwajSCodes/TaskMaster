package com.example.taskmaster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.taskmaster.util.NotificationUtil
import com.example.taskmaster.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        NotificationUtil.showTimerExpired(context)
        PrefUtil.setTimerStateId(TimerActivity.TimerState.Stopped,context)
        PrefUtil.setAlarmSetTime(0,context)

    }
}