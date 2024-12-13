package com.example.to_dolist.screens.splashscreens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource

@Composable
fun SecondSplashScreen(onNext: () -> Unit, onPrev: () -> Unit, onSkip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = com.example.to_dolist.R.drawable.ic_launcher_foreground),
            contentDescription = "Splash Image",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Welcome to Second Splash Screen!")
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onPrev) {
                Text("Prev")
            }
            Button(onClick = onSkip) {
                Text("Skip")
            }
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
}
