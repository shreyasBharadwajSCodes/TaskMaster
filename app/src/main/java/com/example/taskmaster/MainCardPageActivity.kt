package com.example.taskmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmaster.databinding.ActivityMainCardPageBinding

class MainCardPageActivity : AppCompatActivity() {

    lateinit var bind:ActivityMainCardPageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainCardPageBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.worknowcardview.setOnClickListener {
            val i = Intent(this@MainCardPageActivity,TimerActivity::class.java);
            startActivity(i)
        }
        bind.storeTaskSchedule.setOnClickListener {
            val i = Intent(this@MainCardPageActivity,TimerActivity::class.java)
            startActivity(i)
        }
    }
}