package com.example.to_dolist.auth.signup




import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.to_dolist.R
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.navigation.NavigationRoute


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignupScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authViewModel: AuthViewModel = viewModel()
    val authError by authViewModel.authError

    var emailError by remember { mutableStateOf<String?>(null) }


    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(true) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val nameFocusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var showNameError by remember { mutableStateOf(false) }
    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }

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

        Text(
            text = "Hello Register to get started",
            color = Color.Black,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 25.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(44.dp))

        // Name Field
        Text(
            text = "Enter Name",
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp, start = 30.dp),
            fontSize = 19.sp,
            textAlign = TextAlign.Start
        )
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            modifier = Modifier
                .padding(top = 8.dp, start = 4.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .width(500.dp),
            placeholder = { Text("ex : John Doe") },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
            ),
            keyboardActions = KeyboardActions(
                onNext = { emailFocusRequester.requestFocus() }
            ),
        )
        if (showNameError && nameState.value.isEmpty()) {
            Text("Name is required", color = Color.Red, modifier = Modifier.padding(start = 16.dp))
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Email Field
        Text(
            text = "Enter Email",
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp, start = 30.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Start
        )
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            modifier = Modifier
                .focusRequester(emailFocusRequester)
                .padding(top = 8.dp, start = 4.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .width(500.dp),
            placeholder = { Text("ex : hello@email.com") },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                autoCorrect = false,
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
        )
        if (showEmailError && emailState.value.isEmpty()) {
            Text("Email is required", color = Color.Red, modifier = Modifier.padding(start = 16.dp))
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Password Field
        Text(
            text = "Enter Password",
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp, start = 30.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Start
        )
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            modifier = Modifier
                .padding(top = 8.dp, start = 4.dp)
                .fillMaxWidth(0.9f)
                .focusRequester(passwordFocusRequester)
                .align(Alignment.CenterHorizontally)
                .width(500.dp),
            placeholder = { Text("Password") },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
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
        )
        if (showPasswordError && passwordState.value.isEmpty()) {
            Text("Password is required", color = Color.Red, modifier = Modifier.padding(start = 16.dp))
        }

        Spacer(modifier = Modifier.height(27.dp))

        // Register Button
        Button(
            onClick = {
                showNameError = nameState.value.isEmpty()
                showEmailError = emailState.value.isEmpty()
                showPasswordError = passwordState.value.isEmpty()

                if (!showNameError && !showEmailError && !showPasswordError) {
                    authViewModel.signupUser(
                        email = emailState.value,
                        password = passwordState.value,
                        name = nameState.value,
                        onSuccess = {
                            nameState.value = ""
                            emailState.value = ""
                            passwordState.value = ""
                            navController.navigate(NavigationRoute.HomeScreen.route)
                        },
//                        onError = { errorMessage ->
//                            // Handle error (e.g., show a Snackbar or update a state variable)
//                            emailError = errorMessage
//                        }
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(text = "Register", color = Color.White, fontSize = 25.sp)
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

        Text("                             Or Register with",
            color = Color.Magenta,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Login Redirect
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account! ",
                color = Color.Gray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(90.dp))
            Text(
                text = "Login",
                color = Color.Blue,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate(NavigationRoute.LoginScreen.route) }),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}



