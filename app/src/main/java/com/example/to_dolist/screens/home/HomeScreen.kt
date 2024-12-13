package com.example.to_dolist.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.to_dolist.PreferenceManagerHelper
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.navigation.NavigationRoute

@Composable
fun HomeScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel() // Make sure AuthViewModel is initialized
    val preferenceManagerHelper = PreferenceManagerHelper(LocalContext.current) // Initialize PreferenceManagerHelper

    // Initialize ViewModel with PreferenceManagerHelper
    LaunchedEffect(key1 = Unit) {
        authViewModel.initialize(preferenceManagerHelper)
    }

    Column {
        Text("Welcome to Home Screen")
    }

    Button(
        onClick = {
            // Clear user session and navigate to the Welcome Screen
            authViewModel.logoutUser()

            // Navigate to Welcome Screen and clear HomeScreen from the back stack
            navController.navigate(NavigationRoute.WelcomeScreen.route) {
                popUpTo(NavigationRoute.HomeScreen.route) { inclusive = true }
                launchSingleTop = true
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Sign Out")
    }
}
