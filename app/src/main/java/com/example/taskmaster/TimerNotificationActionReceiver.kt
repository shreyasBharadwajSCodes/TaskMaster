package com.example.taskmaster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.taskmaster.util.NotificationUtil
import com.example.taskmaster.util.PrefUtil
import java.util.*

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       when(intent.action)
       {
           AppConstants.ACTION_STOP -> {
               TimerActivity.removeAlarm(context)
               PrefUtil.setTimerStateId(TimerActivity.TimerState.Stopped,context)
               NotificationUtil.hideTimerNotification(context)
           }
           AppConstants.ACTION_PAUSE -> {
                var secondsRemaining =  PrefUtil.getSecondsRemainingId(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerActivity.nowSeconds

                secondsRemaining -=  nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemainingId(secondsRemaining,context)
               TimerActivity.removeAlarm(context)
               PrefUtil.setTimerStateId(TimerActivity.TimerState.Paused,context)
               NotificationUtil.showTimerPaused(context)
           }
           AppConstants.ACTION_RESUME -> {
               var secondsRemaining =  PrefUtil.getSecondsRemainingId(context)
               val wakeuptime = TimerActivity.setAlarm(context,TimerActivity.nowSeconds,secondsRemaining)
               PrefUtil.setTimerStateId(TimerActivity.TimerState.Running,context)
               NotificationUtil.showTimerRunning(context,wakeuptime)
           }
           AppConstants.ACTION_START -> {
               var minutesRemaining = PrefUtil.getTimerLength(context)
               val secondsRemaining = minutesRemaining * 60L
               val wakeuptime = TimerActivity.setAlarm(context,TimerActivity.nowSeconds,secondsRemaining)
               PrefUtil.setTimerStateId(TimerActivity.TimerState.Running,context)
               PrefUtil.setSecondsRemainingId(secondsRemaining,context)
               NotificationUtil.showTimerRunning(context,wakeuptime)

           }
       }
    }
}