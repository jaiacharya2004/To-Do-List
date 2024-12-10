package com.example.to_dolist.auth.login.forgotpassword

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.navigation.NavigationRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var emailState by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") } // To show success message
    val keyboardController = LocalSoftwareKeyboardController.current


    Row (
        modifier = Modifier
            .padding(top = 55.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .clickable {
                    navController.navigate(NavigationRoute.LoginScreen.route)
                }
                .size(48.dp)
                .padding(8.dp)
        )
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Spacer(modifier = Modifier.height(24.dp))




        Text(
            text = "Forgot Password?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Email Input
        OutlinedTextField(
            value = emailState,
            onValueChange = {
                emailState = it
                emailError = ""
            },
            label = { Text("Enter your registered email", color = Color.Black) },
            isError = emailError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.9f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,         // Color for focused state
                unfocusedBorderColor = Color.Gray,       // Color for unfocused state
            )

        )

        // Error Message
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Reset Password Button
        Button(
            onClick = {
                if (emailState.isEmpty()) {
                    emailError = "Email cannot be empty"
                } else {
                    authViewModel.resetPassword(
                        email = emailState,
                        onSuccess = {
                            successMessage = "Password reset email sent. Please check your email."

                        },
                        onError = { errorMessage ->
                            emailError = "email was not sent. Please try again "
                        }
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.fillMaxWidth(0.7f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Reset Password", color = Color.White, fontSize = 16.sp)
        }

        // Error Message
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Success Message
        if (successMessage.isNotEmpty()) {
            Text(
                text = successMessage,
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
