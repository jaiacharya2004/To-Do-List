package com.example.to_dolist

import android.content.Context
import androidx.preference.PreferenceManager

class PreferenceManagerHelper(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    // Key for storing login state
    companion object {
        private const val LOGIN_STATE_KEY = "is_logged_in"
    }

    // Save login state
    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit()
            .putBoolean(LOGIN_STATE_KEY, isLoggedIn)
            .apply() // Apply changes asynchronously
    }

    // Get login state
    fun getLoginState(): Boolean {
        return sharedPreferences.getBoolean(LOGIN_STATE_KEY, false)
    }

    // Clear login state
    fun logoutUser () {
        sharedPreferences.edit()
            .remove(LOGIN_STATE_KEY)
            .apply()
    }

}
