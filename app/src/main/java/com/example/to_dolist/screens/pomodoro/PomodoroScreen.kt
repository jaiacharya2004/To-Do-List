package com.example.to_dolist.screens.pomodoro

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.to_dolist.screens.bottombar.BottomBar

@Composable
fun PomodoroScreen(navController: NavHostController, pomodoroViewModel: PomodoroViewModel) {
    val timerText = pomodoroViewModel.timerText
    val isTimerRunning = pomodoroViewModel.isTimerRunning
    val isPaused = pomodoroViewModel.isPaused
    val timeLeft = pomodoroViewModel.timeLeft
    val isPomodoroCompleted = timeLeft == 0L
    val isBreakTime = pomodoroViewModel.isBreakTime


    // Get the selected time from the ViewModel (default 25 minutes)
    val totalDuration = if (isBreakTime) {
        pomodoroViewModel.breakDuration * 60 * 1000 // Convert selected minutes to milliseconds
    } else {
        pomodoroViewModel.selectedTimeInMinutes * 60 * 1000 // Convert selected minutes to milliseconds
    }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(top = 50.dp, start = 1.dp, end = 1.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = if (isBreakTime) "Break Timer" else "Pomodoro Timer",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Circular Progress Indicator
        // Circular Progress Indicator
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(0.96f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 3

                // Calculate sweep angle for the timer
                val sweepAngle = if (timeLeft == 0L) {
                    0f // When the timer ends, show no progress
                } else if (isTimerRunning || isPaused) {
                    (timeLeft.toFloat() / totalDuration) * 360f // Remaining progress
                } else {
                    360f // Start fully filled
                }

                // Draw the background circle
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.7f),
                    radius = radius,
                    style = Stroke(width = 32f)
                )

                // Draw the timer arc (progress indicator)
                drawArc(
                    color = if (isBreakTime) Color.Cyan else Color.Green,
                    startAngle = 270f, // Start at the top
                    sweepAngle = sweepAngle, // Dynamic angle
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = (size.width - radius * 2) / 2,
                        y = (size.height - radius * 2) / 2
                    ),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 32f)
                )
            }

            // Display the time in the center
            Text(
                text = timerText,
                color = Color.White,
                fontSize = 50.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }


        Spacer(modifier = Modifier.height(30.dp))

        // Duration Slider to select Pomodoro time (1 to 90 minutes)
        Text(
            text = if (isBreakTime) "Set Break Duration: ${pomodoroViewModel.breakDuration} min"
            else "Set Timer Duration: ${pomodoroViewModel.selectedTimeInMinutes} min",
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 18.sp
        )

        Slider(
            value = if (isBreakTime) pomodoroViewModel.breakDuration.toFloat() else pomodoroViewModel.selectedTimeInMinutes.toFloat(),
            onValueChange = { newValue ->
                pomodoroViewModel.updateTimeLeftFromSlider(newValue.toInt())
            },
            valueRange = 1f..90f,
            steps = 89,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )






        Spacer(modifier = Modifier.height(1.dp))

        if (isPomodoroCompleted) {
            // Show "Start Break" and "Skip" buttons when the Pomodoro ends
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { pomodoroViewModel.startBreak() }, // Implement `startBreak` in ViewModel
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Start Break", color = Color.White)
                }

                Button(
                    onClick = { pomodoroViewModel.skipBreak() }, // Implement `skipBreak` in ViewModel
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Skip", color = Color.White)
                }
            }
        } else if (!isTimerRunning && !isPaused) {
            // Show "Start" button
            Button(
                onClick = { pomodoroViewModel.startTimer() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Start", color = Color.White)
            }
        } else if (isTimerRunning) {
            // Show "Pause" and "Restart" buttons
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { pomodoroViewModel.pauseTimer() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Pause", color = Color.White)
                }

                Button(
                    onClick = { pomodoroViewModel.restartTimer() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Restart", color = Color.White)
                }
            }
        } else if (isPaused) {
            // Show "Resume" and "Restart" buttons
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { pomodoroViewModel.resumeTimer() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Resume", color = Color.White)
                }

                Button(
                    onClick = { pomodoroViewModel.restartTimer() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Restart", color = Color.White)
                }
            }
        }




//        Spacer(modifier = Modifier.height(115.dp))
//
//        // Bottom Bar navigation
//        BottomBar(navController)
    }
    Column(
        modifier = Modifier.padding(top = 795.dp)
    ) {
        BottomBar(navController)
    }
}
