package com.example.to_dolist.screens.pomodoro

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PomodoroViewModel : ViewModel() {
    private var _selectedTimeInMinutes by mutableIntStateOf(25) // Backing property
    var selectedTimeInMinutes: Int
        get() = _selectedTimeInMinutes
        set(value) {
            _selectedTimeInMinutes = value
            // Update timeLeft whenever the slider changes
            timeLeft = value * 60 * 1000L
            updateTimerDisplay()
        }
    var timeLeft by mutableStateOf(25 * 60 * 1000L) // Time left in milliseconds
    var isTimerRunning by mutableStateOf(false)
    var isPaused by mutableStateOf(false) // To track if the timer is paused
    var timerText by mutableStateOf("25:00")
    private var countdownTimer: CountDownTimer? = null

    // Start the timer
    fun startTimer() {
        if (!isPaused) {
            // Start with the full selected time if not resuming
            timeLeft = selectedTimeInMinutes * 60 * 1000L
        }

        countdownTimer?.cancel() // Cancel any existing timer
        countdownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateTimerDisplay()
            }

            override fun onFinish() {
                timeLeft = 0
                updateTimerDisplay()
                isTimerRunning = false
                isPaused = false
            }
        }.start()

        isTimerRunning = true
        isPaused = false
    }

    // Pause the timer
    fun pauseTimer() {
        countdownTimer?.cancel()
        isTimerRunning = false
        isPaused = true
    }

    // Resume the timer from where it was paused
    fun resumeTimer() {
        if (isPaused) {
            startTimer() // Start the timer using the remaining `timeLeft`
        }
    }

    // Restart the timer to the default time
    fun restartTimer() {
        countdownTimer?.cancel()
        timeLeft = selectedTimeInMinutes * 60 * 1000L // Reset to full time
        isTimerRunning = false
        isPaused = false
        updateTimerDisplay()
    }

    // Update the timer display (e.g., "MM:SS")
    private fun updateTimerDisplay() {
        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60
        timerText = String.format("%02d:%02d", minutes, seconds)
    }
}
