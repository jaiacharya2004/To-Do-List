package com.example.to_dolist.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import com.example.to_dolist.R
import com.example.to_dolist.navigation.NavigationRoute

@Composable
fun WelcomeScreen(navController: NavHostController) {
    // State to control visibility of the text
    val isVisible = remember { mutableStateOf(false) }

    // Trigger the animation with a delay
    LaunchedEffect(Unit) {
        delay(500) // Delay before showing the text
        isVisible.value = true
    }

    Column(
        modifier = Modifier
            .padding(top = 40.dp)
            .verticalScroll(rememberScrollState()) // Enable vertical scrolling
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White) // Set the background color to white
    ) {
        // Logo
        Image(
            painter = painterResource(R.drawable.welcome_3),
            contentDescription = "Welcome Screen Logo",
            alignment = Alignment.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(40.dp))




        Text(
            text = "Welcome to TaskMate – Simplify Your Day, Achieve Your Goals!",
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color.Black,
            fontSize = 18.sp
        )





        Spacer(modifier = Modifier.height(340.dp)) // Adjusted spacing for the buttons

        // Login Button - Filled
        Button(
            onClick = {navController.navigate(NavigationRoute.LoginScreen.route) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth(0.7f)

                .align(Alignment.CenterHorizontally),
//                .padding(16.dp) ,
            shape = RoundedCornerShape(18.dp),
        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp)) // Adjusted spacing for the buttons

        // Register Button - Filled
        Button(
            onClick = { navController.navigate(NavigationRoute.SignupScreen.route) },
            modifier = Modifier

                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black, // Text color (black)
                disabledContentColor = Color.Gray // Color when button is disabled (optional)
            ),
            border = BorderStroke(1.dp, Color.Black) // Border color (black)
        ) {
            Text(
                text = "Register",
                color = Color.Black, // Black text color
                fontSize = 25.sp
            )
        }

    }
}

