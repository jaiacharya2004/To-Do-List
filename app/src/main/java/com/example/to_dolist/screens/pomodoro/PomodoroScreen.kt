package com.example.to_dolist.screens.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.to_dolist.screens.bottombar.BottomBar

@Composable
fun PomodoroScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, top = 150.dp)
            .verticalScroll(rememberScrollState()) // Enable vertical scrolling
            .background(Color.White)
            .fillMaxSize()
    ) {
        Text("Pomodoro Screen")

        Spacer(modifier = Modifier.height(660.dp))

        BottomBar(navController)
    }

}