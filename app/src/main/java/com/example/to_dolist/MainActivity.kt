package com.example.to_dolist

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.to_dolist.navigation.SetupNavigation
import com.example.to_dolist.ui.theme.ToDoListTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)



        setContent {
            ToDoListTheme {
                SetupNavigation(
                    context = this,
                )
            }
        }
    }
}
