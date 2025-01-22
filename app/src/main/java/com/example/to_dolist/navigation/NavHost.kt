package com.example.to_dolist.navigation

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_dolist.PreferenceManagerHelper
import com.example.to_dolist.auth.WelcomeScreen
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.auth.login.LoginScreen
import com.example.to_dolist.auth.login.forgotpassword.ForgotPasswordScreen
import com.example.to_dolist.auth.signup.SignupScreen


import com.example.to_dolist.screens.home.HomeScreen
import com.example.to_dolist.screens.pomodoro.PomodoroScreen
import com.example.to_dolist.screens.pomodoro.PomodoroViewModel
import com.example.to_dolist.screens.profile.ProfileScreen
import com.example.to_dolist.screens.profile.ProfileViewModel
import com.example.to_dolist.screens.splashscreens.SplashManager
import com.example.to_dolist.screens.states.StateScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostGraph(navController: NavHostController, context: Context, authViewModel: AuthViewModel) {
    val preferenceManagerHelper = PreferenceManagerHelper(context)

    authViewModel.initialize(preferenceManagerHelper)

    val pomodoroViewModel: PomodoroViewModel = viewModel()
    // Use context to initialize ProfileRepository, not ProfileImageDao directly

    Log.d("ViewModelValue", "$pomodoroViewModel")

    NavHost(navController = navController, startDestination = NavigationRoute.Splash.route) {
        composable(NavigationRoute.Splash.route) {
            SplashManager(navController = navController, context = context)
        }
        composable(NavigationRoute.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }
        composable(NavigationRoute.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(NavigationRoute.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController, authViewModel = authViewModel)
        }
        composable(NavigationRoute.SignupScreen.route) {
            SignupScreen(navController, authViewModel = authViewModel)
        }
        composable(NavigationRoute.HomeScreen.route) {
            HomeScreen(
                navController, ProfileViewModel(context),
            )
        }
        composable(NavigationRoute.ProfileScreen.route) {
            ProfileScreen(navController = navController, ProfileViewModel(context))
        }
        composable(NavigationRoute.PomodoroScreen.route) {
            PomodoroScreen(navController, pomodoroViewModel)
        }
        composable(NavigationRoute.StateScreen.route) {
            StateScreen(navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavigation(context: Context)  {
    val navController = rememberNavController() // Initialize navController only once here
    val authViewModel: AuthViewModel = viewModel() // Obtain AuthViewModel here
    NavHostGraph(navController, context, authViewModel) // Pass the navController and context to NavHostGraph
}

sealed class NavigationRoute(val route: String) {
    data object Splash : NavigationRoute("splash")
    data object WelcomeScreen : NavigationRoute("welcome")
    data object LoginScreen : NavigationRoute("login")
    data object SignupScreen : NavigationRoute("signup")
    data object ForgotPasswordScreen : NavigationRoute("forgot_password_screen")
    data object HomeScreen : NavigationRoute("home")
    data object ProfileScreen : NavigationRoute("profile")
    data object PomodoroScreen : NavigationRoute("pomodoro")
    data object StateScreen : NavigationRoute("states")
}
