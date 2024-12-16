package com.example.to_dolist.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.to_dolist.PreferenceManagerHelper
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.navigation.NavigationRoute
import com.example.to_dolist.screens.bottombar.BottomBar

@Composable
fun HomeScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel() // Make sure AuthViewModel is initialized
    val preferenceManagerHelper = PreferenceManagerHelper(LocalContext.current) // Initialize PreferenceManagerHelper

    // Initialize ViewModel with PreferenceManagerHelper
    LaunchedEffect(key1 = Unit) {
        authViewModel.initialize(preferenceManagerHelper)
    }

    Column (
        modifier = Modifier
            .padding(top = 20.dp, start = 1.dp, end = 1.dp)
            .verticalScroll(rememberScrollState()) // Enable vertical scrolling
            .background(Color.White)
            .fillMaxSize()

        ) {
        Text("Welcome to Home Screen")


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
        Spacer(modifier = Modifier.height(680.dp))

        BottomBar(navController)
  }
}
