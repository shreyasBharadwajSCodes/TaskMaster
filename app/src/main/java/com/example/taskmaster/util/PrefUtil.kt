package com.example.taskmaster.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.taskmaster.TimerActivity

class PrefUtil {
    companion object{

        private const val TIMER_LENGTH = "com.example.taskmaster.timer_length"
        fun getTimerLength(context:Context):Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH, 10)
        }
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.example.taskmaster.previous_timer_length"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID,0)
        }
        fun setPreviousTimerLengthSeconds(seconds: Long,context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID,seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.example.taskmaster.timer_state_id"
        fun getTimerStateId(context: Context): TimerActivity.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal =  preferences.getInt(TIMER_STATE_ID,0)
            return TimerActivity.TimerState.values()[ordinal]
        }
        fun setTimerStateId(state:TimerActivity.TimerState,context: Context)  {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal =  state.ordinal
            editor.putInt(TIMER_STATE_ID,ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "com.example.taskmaster.seconds_remaining_id"

        fun getSecondsRemainingId(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID,0)
        }
        fun setSecondsRemainingId(seconds: Long,context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID,seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "com.example.taskmaster.background_time"

        fun getAlarmSetTime(context : Context) : Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID,0)
        }

        fun setAlarmSetTime(time:Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID,time)
            editor.apply()
        }

    }
}