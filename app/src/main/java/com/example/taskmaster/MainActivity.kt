package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    lateinit var landingpageintent:Intent
    private var timeLimit = 2500L
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@MainActivity.options()
            //this@MainActivity.finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        landingpageintent = Intent(this@MainActivity,LoginActivity::class.java)
        //CHECK_FOR_UPDATES
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(landingpageintent)
        }, timeLimit)

    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(landingpageintent)
        }, timeLimit)
    }

    fun options(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning")
            .setMessage("Do you really want to close the app?")
            .setPositiveButton("Yes") { _, _ -> System.exit(0) }
            .setNegativeButton("No", null)
            .show()
    }
}
    /**
     * Launcher activity
     * Check for updates to be made
     */