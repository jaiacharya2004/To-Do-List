package com.example.to_dolist.screens.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ProfileViewModel(private val context: Context) : ViewModel() {

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri




    init {
        viewModelScope.launch {
            val savedUri = loadSavedImageUri()
            _profileImageUri.emit(savedUri) // Emit the loaded URI to the StateFlow
        }
    }


    fun updateProfileImage(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                val filePath = saveImageToInternalStorage(it)
                filePath?.let {
                    _profileImageUri.emit(Uri.parse(it)) // Update state with file URI
                    saveImageUri(Uri.parse(it)) // Save the file path as URI
                }
            }
        }
    }

    private fun loadSavedImageUri(): Uri? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val filePath = sharedPreferences.getString("profile_image_uri", null)
        Log.d("ProfileViewModel", "Loaded File Path: $filePath")
        return filePath?.let { Uri.fromFile(File(it)) }
    }



    private fun saveImageUri(uri: Uri?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString("profile_image_uri", uri?.toString())
            apply()
        }
        Log.d("ProfileViewModel", "Saved URI: ${uri?.toString()}")
    }


    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath // Return the file path
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}