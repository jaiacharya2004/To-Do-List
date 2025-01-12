package com.example.to_dolist

import android.content.Context
import android.net.Uri
import androidx.preference.PreferenceManager

class PreferenceManagerHelper(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    // Key for storing login state
    companion object {
        private const val LOGIN_STATE_KEY = "is_logged_in"
        private const val PROFILE_IMAGE_URI_KEY = "profile_image_uri" // Ensure this key is defined
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

    // Save profile image URI
    fun saveProfileImageUri(uri: Uri) {
        sharedPreferences.edit()
            .putString(PROFILE_IMAGE_URI_KEY, uri.toString()) // Save the URI as a string
            .apply()
    }

    // Get saved profile image URI
    fun getProfileImageUri(): Uri? {
        val uriString = sharedPreferences.getString(PROFILE_IMAGE_URI_KEY, null)
        return if (uriString != null) Uri.parse(uriString) else null
    }


}
