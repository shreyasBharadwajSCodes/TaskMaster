package com.example.taskmaster

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.taskmaster.databinding.ActivityTimerBinding
import com.example.taskmaster.util.NotificationUtil
import com.example.taskmaster.util.PrefUtil
import java.util.*
import java.util.prefs.Preferences

class TimerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTimerBinding

    companion object{
            fun setAlarm(context: Context, nowSeconds:Long,secondsRemaining:Long): Long{
                val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context,TimerExpiredReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_MUTABLE)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,wakeUpTime,pendingIntent)
                PrefUtil.setAlarmSetTime(nowSeconds,context)
                return wakeUpTime
            }

            fun removeAlarm(context: Context){
                val intent = Intent(context,TimerExpiredReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_MUTABLE)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                PrefUtil.setAlarmSetTime(0,context)
            }
        val nowSeconds:Long
         get() = Calendar.getInstance().timeInMillis/1000
    }

    enum class TimerState{
        Stopped,Paused,Running
    }

    private lateinit var  timer:CountDownTimer
    private var  timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped
    private var  secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setIcon(R.drawable.ic_timer_24)
        supportActionBar?.title = "  Work Timer"

        /*val navController = findNavController(R.id.nav_host_fragment_content_timer
        )
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
*/
        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        binding.fabStart.setOnClickListener{
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }
        binding.fabPause.setOnClickListener{
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }
        binding.fabStop.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()
        initTimer()
        removeAlarm(this)
        NotificationUtil.hideTimerNotification(this) //Notifications here
    }

    override fun onPause() {
        super.onPause()
        if(timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds,secondsRemaining)
            NotificationUtil.showTimerRunning(this,wakeUpTime)
        }
        else if(timerState == TimerState.Paused){
            NotificationUtil.showTimerPaused(this)
        }
        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds,this)
        PrefUtil.setSecondsRemainingId(secondsRemaining,this)
        PrefUtil.setTimerStateId(timerState,this)
    }
    private fun initTimer(){
        timerState = PrefUtil.getTimerStateId(this)
        if(timerState == TimerState.Stopped){
            setNewTimerLength()
        }
        else{
            setPreviousTimerLength()
        }

        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemainingId(this)
            else timerLengthSeconds
        //To-DO
        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if(alarmSetTime > 0)
            secondsRemaining -= nowSeconds  - alarmSetTime
        if(secondsRemaining<=0)
            onTimerFinished()
        else if(timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped
        setNewTimerLength()

        val pg = findViewById<ProgressBar>(R.id.progress_countdown)
        pg.progress = 0

        PrefUtil.setSecondsRemainingId(timerLengthSeconds,this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }
    private fun startTimer(){
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000,1000){
            override fun onFinish() = onTimerFinished()
            override fun  onTick(millisUntillFinished: Long){
                secondsRemaining = millisUntillFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }
    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = lengthInMinutes * 60L
        val pg = findViewById<ProgressBar>(R.id.progress_countdown)
        pg.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        val pg = findViewById<ProgressBar>(R.id.progress_countdown)
        pg.max = timerLengthSeconds.toInt()

    }

    fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()

        val tv = findViewById<TextView>(R.id.textview_first)
        tv.text = "$minutesUntilFinished:${if(secondsStr.length==2) secondsStr else {"0"+ secondsStr}}"

        val pg = findViewById<ProgressBar>(R.id.progress_countdown)
        pg.progress = (timerLengthSeconds - secondsRemaining).toInt()

    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running->{
                binding.fabStart.isEnabled = false
                binding.fabPause.isEnabled = true
                binding.fabStop.isEnabled = true
            }
            TimerState.Stopped->{
                binding.fabStart.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = false
            }
            TimerState.Paused->{
                binding.fabStart.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = true
            }
        }

    }

    override fun onCreateOptionsMenu(menu:Menu): Boolean {
        menuInflater.inflate(R.menu.menu_timer,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this,SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {false}
        }
    }
    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_timer)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}