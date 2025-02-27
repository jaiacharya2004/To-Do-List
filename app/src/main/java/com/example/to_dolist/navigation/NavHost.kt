package com.example.to_dolist.navigation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_dolist.PreferenceManagerHelper
import com.example.to_dolist.screens.splashscreens.WelcomeScreen
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.auth.login.LoginScreen
import com.example.to_dolist.auth.login.forgotpassword.ForgotPasswordScreen
import com.example.to_dolist.auth.signup.SignupScreen
import com.example.to_dolist.data.FirestoreHelper
import com.example.to_dolist.data.repository.TodoRepository


import com.example.to_dolist.screens.home.HomeScreen
import com.example.to_dolist.screens.home.HomeViewModel
import com.example.to_dolist.screens.pomodoro.PomodoroScreen
import com.example.to_dolist.screens.pomodoro.PomodoroViewModel
import com.example.to_dolist.screens.profile.ProfileScreen
import com.example.to_dolist.screens.profile.ProfileViewModel
import com.example.to_dolist.screens.splashscreens.SplashManager
import com.example.to_dolist.screens.states.StateScreen
import com.example.to_dolist.screens.todo.CreateNewTaskScreen
import com.example.to_dolist.screens.todo.EditTaskScreen

@Composable
fun NavHostGraph(navController: NavHostController, context: Context, authViewModel: AuthViewModel) {
    val preferenceManagerHelper = PreferenceManagerHelper(context)
    val todoRepository = TodoRepository()

    val application = context.applicationContext as Application
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
                navController, ProfileViewModel(context), onDelete = { todo ->
                    todoRepository.deleteTodo(todo) // Corrected call
                },
                onRestore = { todo ->
                    todoRepository.addTodo(todo) // Corrected call
                }
            )
        }
        composable(NavigationRoute.ProfileScreen.route) {
            ProfileScreen(
                navController = navController, ProfileViewModel(context),
                authViewModel = AuthViewModel(application),
                savedInstanceState = null
            )
        }
        composable(NavigationRoute.PomodoroScreen.route) {
            PomodoroScreen(navController, pomodoroViewModel)
        }
        composable(NavigationRoute.StateScreen.route) {
            StateScreen(navController)
        }
        composable(NavigationRoute.CreateNewScreen.route) {
            CreateNewTaskScreen(navController, viewmodel = HomeViewModel(), onTaskCreate = { task ->
                // Add the task to Firestore
                val firestoreHelper = FirestoreHelper()
                firestoreHelper.addTodo(task)
            })
        }


        composable("edit_task_screen/{taskId}/{taskName}/{description}/{status}/{category}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            val taskName = backStackEntry.arguments?.getString("taskName") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val status = backStackEntry.arguments?.getString("status") ?: "Pending"
            val category = backStackEntry.arguments?.getString("category") ?: ""

            Log.d("EditTaskScreen", "Received task data: taskId=$taskId, taskName=$taskName, status=$status, category=$category")


            EditTaskScreen(
                taskId = taskId,
                taskName = taskName,
                description = description,
                status = status,
                category = category,
                navController = navController,
                viewModel = HomeViewModel()
            )
        }



    }
}

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
    data object CreateNewScreen : NavigationRoute("create_task_screen")
    data object EditTaskScreen : NavigationRoute("edit_task_screen")
}
