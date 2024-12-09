package com.example.to_dolist.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_dolist.auth.WelcomeScreen
import com.example.to_dolist.auth.login.LoginScreen
import com.example.to_dolist.auth.signup.SignupScreen

@Composable
fun NavHostGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationRoute.WelcomeScreen.route) {
        composable(NavigationRoute.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }
        composable(NavigationRoute.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(NavigationRoute.SignupScreen.route) {
            SignupScreen(navController)
        }
    }
}

@Composable
fun SetupNavigation() {
    val navController = rememberNavController()
    NavHostGraph(navController)
}
sealed class NavigationRoute(val route: String) {
    data object WelcomeScreen : NavigationRoute("welcome")
    data object LoginScreen : NavigationRoute("login")
    data object SignupScreen : NavigationRoute("signup")
}