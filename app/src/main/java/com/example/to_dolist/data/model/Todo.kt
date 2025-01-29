package com.example.to_dolist.data.model

import com.google.firebase.Timestamp

data class Todo(
    val taskName: String = "", // Default empty string
    val category: String = "", // Default empty string
    val description: String = "", // Default empty string
    val date: Timestamp = Timestamp.now(), // Default to current timestamp
    val time: Timestamp? = null , // Default null
    val status : String = ""
)
