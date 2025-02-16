package com.example.to_dolist.screens.splashscreens

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.to_dolist.PreferenceManagerHelper
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.navigation.NavigationRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashManager(
    navController: NavHostController,
    context: Context,
) {
    val authViewModel: AuthViewModel = viewModel()
    val preferenceManagerHelper = PreferenceManagerHelper(context)
    // Initialize the splash screen state
    var currentSplashScreen by remember { mutableIntStateOf(1) }
    val scope = rememberCoroutineScope()

    authViewModel.initialize(preferenceManagerHelper)
    val isLoggedIn = authViewModel.getLoginState()

    // Use LaunchedEffect for splash screen transition delay
    LaunchedEffect(Unit) {
        if (isLoggedIn) {
            navController.navigate(NavigationRoute.HomeScreen.route)
        } else {
            navController.navigate(NavigationRoute.WelcomeScreen.route)
        }
    }

    // Splash screen transitions
    when (currentSplashScreen) {
        1 -> FirstSplashScreen()
        2 -> SecondSplashScreen(
            onNext = { currentSplashScreen = 3 },
            onPrev = { currentSplashScreen = 1 },
            onSkip = { currentSplashScreen = 4 }
        )
        3 -> ThirdSplashScreen(
            onPrev = { currentSplashScreen = 2 },
            onSkip = { currentSplashScreen = 4 }
        )
    }
}


// Function to check if the user is logged in by reading from SharedPreferences
fun isUserLoggedIn(context: Context): Boolean {
    val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getBoolean("is_logged_in", false) // Default to false if not found
}
