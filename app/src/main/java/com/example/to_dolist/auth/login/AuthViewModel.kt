package com.example.to_dolist.auth.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_dolist.PreferenceManagerHelper
import com.example.to_dolist.navigation.NavigationRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _authError = mutableStateOf<String?>(null)
    val authError: State<String?> get() = _authError

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var preferenceManagerHelper: PreferenceManagerHelper

    // Initialize PreferenceManagerHelper
    fun initialize(preferenceManagerHelper: PreferenceManagerHelper) {
        this.preferenceManagerHelper = preferenceManagerHelper
    }

    // Save login state in SharedPreferences
    private fun saveLoginState(isLoggedIn: Boolean) {
        preferenceManagerHelper.saveLoginState(isLoggedIn)
    }

    // Retrieve login state from SharedPreferences
    fun getLoginState(): Boolean {
        return preferenceManagerHelper.getLoginState()
    }

    fun logoutUser () {
        Log.d("AuthViewModel", "Logging out user")
        preferenceManagerHelper.logoutUser()
        Log.d("AuthViewModel", "User logged out")    }


    // Function to handle user login
    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            _authError.value = "Email and Password cannot be empty"
            onError("Email and Password cannot be empty")
            return
        }

        _isLoading.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        saveLoginState(true) // Save login state as true
                    }
                    onSuccess()
                } else {
                    val exception = task.exception
                    Log.e("LoginError", "Exception type: ${exception?.javaClass?.name}")
                    Log.e("LoginError", "Exception message: ${exception?.message}")

                    val errorMessage = when {
                        exception is FirebaseAuthInvalidUserException ||
                                exception?.message?.contains("user not found", ignoreCase = true) == true -> {
                            "Email is not registered. Please register first."
                        }
                        exception is FirebaseAuthInvalidCredentialsException -> {
                            "Invalid email or password. Please try again."
                        }
                        else -> {
                            "Login failed: ${exception?.localizedMessage ?: "Unknown error"}"
                        }
                    }
                    _authError.value = errorMessage
                    onError(errorMessage)
                }
            }
    }

    // Function to handle password reset
    fun resetPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isEmpty()) {
            _authError.value = "Email cannot be empty"
            onError("Email cannot be empty")
            return
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Failed to send reset email"
                    _authError.value = errorMessage
                    onError(errorMessage)
                }
            }
    }

    // Function to handle user signup
    fun signupUser(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _authError.value = "Name, Email, and Password cannot be empty"
            onError("Name, Email, and Password cannot be empty")
            return
        }

        _isLoading.value = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false

                if (task.isSuccessful) {
                    _authError.value = null // Clear error on success
                    val user = auth.currentUser
                    user?.updateProfile(
                        userProfileChangeRequest {
                            displayName = name
                        }
                    )
                    viewModelScope.launch {
                        saveLoginState(true) // Save login state as true
                    }
                    onSuccess()
                } else {
                    val exception = task.exception
                    Log.e("AuthViewModel", "Signup error: ${exception?.message}", exception)

                    val errorMessage = when (exception) {
                        is FirebaseAuthUserCollisionException -> {
                            "This email is already registered. Please Login."
                        }
                        else -> {
                            exception?.localizedMessage ?: "An unknown error occurred."
                        }
                    }
                    _authError.value = errorMessage
                    onError(errorMessage)
                }
            }
    }


}
