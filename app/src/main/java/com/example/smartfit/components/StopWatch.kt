package com.example.smartfit.components

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopWatch {

    private var formattedTime by mutableStateOf("00:00:00:000")

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isRunning = false


    private var timeMillis = 0L
    private var lastTimeStamp = 0L

    fun start() {
        if (isRunning) return
        isRunning = true

        coroutineScope.launch {
            lastTimeStamp = System.currentTimeMillis()

            while (isRunning) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimeStamp
                lastTimeStamp = System.currentTimeMillis()
                formattedTime = formatTime(timeMillis)
            }
        }
    }

    fun isRunning(): Boolean {
        return isRunning
    }

    fun getTimeMillis(): Long {
        return timeMillis
    }

    fun getCustomFormattedTime(): String {
        return formattedTime
    }

    fun pause() {
        isRunning = false
    }

    fun reset() {
        isRunning = false

        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        lastTimeStamp = 0L
        formattedTime = "00:00:00:000"
    }

    @SuppressLint("NewApi")
    private fun formatTime(timeMillis: Long): String {
        val hours = (timeMillis / 3600000) % 24
        val minutes = (timeMillis / 60000) % 60
        val seconds = (timeMillis / 1000) % 60
        val milliseconds = timeMillis % 1000

        return if (hours > 0) {
            String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds)
        } else {
            String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
        }
    }

}