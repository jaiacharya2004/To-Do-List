package com.example.to_dolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.to_dolist.auth.WelcomeScreen
import com.example.to_dolist.auth.login.LoginScreen
import com.example.to_dolist.auth.signup.SignupScreen
import com.example.to_dolist.navigation.SetupNavigation
import com.example.to_dolist.ui.theme.ToDoListTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        enableEdgeToEdge()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            ToDoListTheme {
                // Setting up the navigation graph
                SetupNavigation(context = this)
            }
        }
    }
}