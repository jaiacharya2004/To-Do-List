package com.example.to_dolist.data.model

import com.google.firebase.Timestamp

data class Todo(
    val id: String = "", // Default empty string
    val taskName: String = "", // Default empty string
    val category: String = "", // Default empty string
    val description: String = "", // Default empty string
    val status : String = ""
)
