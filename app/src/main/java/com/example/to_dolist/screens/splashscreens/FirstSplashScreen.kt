package com.example.to_dolist.screens.splashscreens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle

@Composable
fun FirstSplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(46.dp))

        Image(
            painter = painterResource(id = com.example.to_dolist.R.drawable.taskmate_logo),
            contentDescription = "Splash Image",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(216.dp))

        Text("Jai Acharya",color = Color.Black)
    }
}
