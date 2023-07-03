package com.example.taskmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmaster.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //val bind = ActivitySettingsBinding.inflate(layoutInflater)
        setSupportActionBar(findViewById(R.id.toolbarsettings))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
    }
}