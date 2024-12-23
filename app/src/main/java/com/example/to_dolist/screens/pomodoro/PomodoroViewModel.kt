package com.example.to_dolist.screens.pomodoro

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
    var timeLeft by mutableLongStateOf(25 * 60 * 1000L) // Time left in milliseconds
    var isTimerRunning by mutableStateOf(false)
    var isPaused by mutableStateOf(false) // To track if the timer is paused
    var timerText by mutableStateOf("25:00")
    var isBreakTime = false
    var breakDuration: Int by mutableIntStateOf(5) // Default break duration in minutes

    private var countdownTimer: CountDownTimer? = null

    // Start the timer
    fun startTimer() {
        isTimerRunning = true
        isPaused = false

        countdownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateTimerDisplay() // Update the display each second
            }

            override fun onFinish() {
                isTimerRunning = false
                isPaused = false
                timeLeft = 0L
                updateTimerDisplay()
                // Handle the end of the Pomodoro session (e.g., notify user, start break)
            }
        }.start()
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
        // Update the timeLeft according to the current session (break or Pomodoro)
        if (isBreakTime) {
            timeLeft = breakDuration * 60 * 1000L // Break duration
        } else {
            timeLeft = selectedTimeInMinutes * 60 * 1000L // Pomodoro duration
        }
        updateTimerDisplay() // Update display immediately
        isTimerRunning = false
        isPaused = false
    }


    fun startBreak() {
        isBreakTime = true
        timeLeft = breakDuration * 60 * 1000L // Set the break time directly
        updateTimerDisplay()
        startTimer()
    }


    fun skipBreak() {
        isBreakTime = false
        selectedTimeInMinutes = 25 // Default Pomodoro duration
        resetTimer()
        startTimer() // Automatically start the next Pomodoro session
    }

    private fun resetTimer() {
        isTimerRunning = false
        isPaused = false
        timeLeft = selectedTimeInMinutes * 60 * 1000L
        isBreakTime = false // Reset the break state
    }

    fun updateTimeLeftFromSlider(newValue: Int) {
        countdownTimer?.cancel() // Stop the timer if it's running
        isTimerRunning = false
        isPaused = false

        if (isBreakTime) {
            breakDuration = newValue
            timeLeft = newValue * 60 * 1000L
        } else {
            selectedTimeInMinutes = newValue
            timeLeft = newValue * 60 * 1000L
        }

        updateTimerDisplay()
    }




    // Update the timer display (e.g., "MM:SS")
    private fun updateTimerDisplay() {
        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60
        timerText = String.format("%02d:%02d", minutes, seconds)
    }
}