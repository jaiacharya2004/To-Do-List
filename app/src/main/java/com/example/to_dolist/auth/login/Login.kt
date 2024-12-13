package com.example.to_dolist.auth.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.to_dolist.PreferenceManagerHelper
import com.example.to_dolist.R
import com.example.to_dolist.navigation.NavigationRoute

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavHostController) {


    val context = LocalContext.current
    val preferenceManagerHelper = remember { PreferenceManagerHelper(context) }

    val authViewModel: AuthViewModel = viewModel()
    authViewModel.initialize(preferenceManagerHelper)

    val authError by authViewModel.authError

// Initialize the DataStore instance inside DataStoreManager

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(true) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }






    Column(
        modifier = Modifier
            .padding(top = 50.dp)
            .background(Color.White)
            .fillMaxSize()
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .clickable {
                        navController.navigate(NavigationRoute.WelcomeScreen.route)
                    }
                    .size(44.dp)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = "Welcome Back! Glad to see you again!",
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(44.dp))

        Text(
            text = "Enter Email",
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp, start = 30.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Start
        )
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { newValue ->
                emailState.value = newValue
                if (newValue.isNotEmpty()) emailError = ""
            },
            modifier = Modifier
                .focusRequester(emailFocusRequester)
                .padding(top = 8.dp, start = 4.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .width(500.dp),
            placeholder = { Text("ex: hello@email.com") },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    passwordFocusRequester.requestFocus()
                }
            ),
            isError = emailError.isNotEmpty()
        )

        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Enter Password",
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp, start = 30.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Start
        )
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { newValue ->
                passwordState.value = newValue
                if (newValue.isNotEmpty()) passwordError = ""
            },
            modifier = Modifier
                .focusRequester(passwordFocusRequester)
                .padding(top = 8.dp, start = 4.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .width(500.dp),
            placeholder = { Text("Password") },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    val icon: Painter = if (passwordVisibility) {
                        painterResource(id = R.drawable.visibility)
                    } else {
                        painterResource(id = R.drawable.red)
                    }
                    Icon(painter = icon, contentDescription = "Toggle visibility")
                }
            },
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError.isNotEmpty()
        )

        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Forgot Password?",
            color = Color.Magenta,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    navController.navigate(NavigationRoute.ForgotPasswordScreen.route)
                }
                .align(Alignment.End)
                .padding(end = 30.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                emailError = ""
                passwordError = ""

                if (emailState.value.isEmpty()) {
                    emailError = "Email cannot be empty"
                }
                if (passwordState.value.isEmpty()) {
                    passwordError = "Password cannot be empty"
                }

                if (emailState.value.isNotEmpty() && passwordState.value.isNotEmpty()) {
                    authViewModel.loginUser(
                        email = emailState.value,
                        password = passwordState.value,
                        onSuccess = {
                            emailState.value = ""
                            passwordState.value = ""
                            navController.navigate(NavigationRoute.HomeScreen.route) {
                                // Pop previous screens to ensure the user cannot go back to the login screen
                                popUpTo(NavigationRoute.LoginScreen.route) { inclusive = true }
                            }
                        },
                        onError = { errorMessage ->
                            // Display the error message in the UI
                            emailError = errorMessage
                        }
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Login", color = Color.White, fontSize = 25.sp)
        }


        if (!authError.isNullOrEmpty()) {
            Text(
                text = authError!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp, start = 10.dp),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Or Login with",
            color = Color.Magenta,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.Gray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Register",
                color = Color.Blue,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate(NavigationRoute.SignupScreen.route) })
                    .padding(top = 8.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
