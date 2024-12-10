package com.example.to_dolist.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.to_dolist.auth.login.AuthViewModel

@Composable
fun HomeScreen (
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    Column {
        Text("Welcome to Home Screen")
    }
}