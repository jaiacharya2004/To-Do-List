package com.example.to_dolist.auth.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // UI States
    private var isLoading = mutableStateOf(false)
    private val _authError = mutableStateOf<String?>(null)
    val authError: State<String?> get() = _authError

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            _authError.value = "Email and Password cannot be empty"
            return
        }

        isLoading.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val exception = task.exception
                    Log.e("LoginError", "Exception type: ${exception?.javaClass?.name}")
                    Log.e("LoginError", "Exception message: ${exception?.message}")

                    when {
                        exception is FirebaseAuthInvalidUserException ||
                                exception?.message?.contains("user not found", ignoreCase = true) == true -> {
                            _authError.value = "Email is not registered. Please register first."
                        }
                        exception is FirebaseAuthInvalidCredentialsException -> {
                            _authError.value = "Invalid email or password. Please try again."
                        }
                        else -> {
                            _authError.value = "Login failed: ${exception?.localizedMessage ?: "Unknown error"}"
                        }
                    }

                }
            }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Failed to send reset email"
                    onError(errorMessage)
                }
            }
    }


    fun signupUser(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _authError.value = "Name, Email, and Password cannot be empty"
            return
        }

        isLoading.value = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false

                if (task.isSuccessful) {
                    _authError.value = null // Clear error on success
                    val user = auth.currentUser
                    user?.updateProfile(
                        userProfileChangeRequest {
                            displayName = name
                        }
                    )
                    onSuccess()
                } else {
                    val exception = task.exception
                    Log.e("AuthViewModel", "Signup error: ${exception?.message}", exception)

                    _authError.value = when (exception) {
                        is FirebaseAuthUserCollisionException -> {
                            "This email is already registered. Please Login."
                        }
                        else -> {
                            exception?.localizedMessage ?: "An unknown error occurred."
                        }
                    }
                }
            }
    }
}
