package com.example.foodorderingapp

import android.os.SystemClock
import android.view.View

fun View.setSafeOnClickListener(interval: Long = 1000L, onSafeClick: (View) -> Unit) {
    var lastClickTime = 0L

    this.setOnClickListener {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime > interval) {
            lastClickTime = currentTime
            onSafeClick(it)
        }
    }
}