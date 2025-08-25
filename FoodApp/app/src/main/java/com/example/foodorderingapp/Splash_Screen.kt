package com.example.foodorderingapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,Start::class.java)
            startActivity(intent)
            finish()
        }, 3000)
        //Handler() by default used the current thread’s Looper, but this implicit behavior is now deprecated.
        //Handler(Looper.getMainLooper()) makes it explicit that you're posting to the main UI thread — which is best practice and avoids warnings
    }
}