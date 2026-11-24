package com.example.coroutinedemo.data

data class Task(
    val id: Int,
    val title: String,
    val course: String,
    val deadline: String = "Besok",
    val isCompleted: Boolean = false
)