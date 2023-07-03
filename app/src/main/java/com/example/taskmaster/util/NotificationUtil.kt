package com.example.taskmaster.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.taskmaster.AppConstants
import com.example.taskmaster.R
import com.example.taskmaster.TimerActivity
import com.example.taskmaster.TimerNotificationActionReceiver
import java.text.SimpleDateFormat
import java.util.*


class NotificationUtil {
    companion object{
        private lateinit var curcontext:Context
        private const val CHANNEL_ID_TIMER =  "menu_timer"
        private const val CHANNEL_NAME_TIMER =  "TaskMaster App Timer"
        private const val TIMER_ID = 0

        fun showTimerExpired(context: Context){
            var startIntent = Intent(context,TimerNotificationActionReceiver::class.java)
            startIntent.action = AppConstants.ACTION_START

            val startPendingIntent = PendingIntent.getBroadcast(context,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER,true)
            nBuilder.setContentTitle("Timer Expired!")
                    .setContentText("start again?")
                .setContentIntent(getPendingIntentWithStack(context,TimerActivity::class.java))
                .addAction(R.drawable.ic_baseline_play_arrow_24,"Start",startPendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            curcontext = context
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
            nManager.notify(TIMER_ID,nBuilder.build())
        }

        fun showTimerRunning(context: Context, wakeUpTime: Long){

            var stopIntent = Intent(context,TimerNotificationActionReceiver::class.java)
            stopIntent.action = AppConstants.ACTION_STOP

            val stopPendingIntent = PendingIntent.getBroadcast(context,0,stopIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

            var pauseIntent = Intent(context,TimerNotificationActionReceiver::class.java)
            pauseIntent.action = AppConstants.ACTION_PAUSE

            val pausePendingIntent = PendingIntent.getBroadcast(context,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER,true)
            nBuilder.setContentTitle("Timer is Running!")
                .setContentText("Alarm will ring at : ${df.format(Date(wakeUpTime))}")
                .setContentIntent(getPendingIntentWithStack(context,TimerActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_baseline_stop_24,"Stop",stopPendingIntent)
                .addAction(R.drawable.ic_baseline_pause_24,"Pause",pausePendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
            nManager.notify(TIMER_ID,nBuilder.build())
        }

        fun showTimerPaused(context: Context){
            var resumeIntent = Intent(context,TimerNotificationActionReceiver::class.java)
            resumeIntent.action = AppConstants.ACTION_PAUSE

            val resumePendingIntent = PendingIntent.getBroadcast(context,0,resumeIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER,true)
            nBuilder.setContentTitle("Timer is Paused!")
                .setContentText("Resume?")
                .setContentIntent(getPendingIntentWithStack(context,TimerActivity::class.java))
                .addAction(R.drawable.ic_baseline_play_arrow_24,"Resume",resumePendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
            nManager.notify(TIMER_ID,nBuilder.build())
        }

        fun hideTimerNotification(context: Context){
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }

        private fun getBasicNotificationBuilder(context: Context, channelId: String, playsound: Boolean): NotificationCompat.Builder {
            //val notificationSound:Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationSound:Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/alarm_sound_for_app") //+ R.raw.alarm_sound_for_app
            val nBuilder = NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_timer_24)
                .setAutoCancel(true)
                .setDefaults(0)
            if(playsound){
                nBuilder.setSound(notificationSound)
            }
            return nBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context,javaClass: Class<T>): PendingIntent{
            val resultIntent = Intent(context,javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        }

        private fun NotificationManager.createNotificationChannel(channelId: String,channelName: String ,playsound: Boolean){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelImportance = if(playsound)NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW

                val nChannel = NotificationChannel(channelId,channelName,channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor = Color.BLUE
                //nChannel.enableVibration(true)
                /*val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                nChannel.setSound(Uri.parse("android.resource://"+curcontext?.getPackageName()+"/"+R.raw.alarm_sound_for_app),audioAttributes)*/
                this.createNotificationChannel(nChannel)
            }
        }
    }
}