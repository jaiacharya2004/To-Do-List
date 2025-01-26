package com.example.to_dolist.data.model

data class Todo(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val alarm: Boolean = false
)
