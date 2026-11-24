package com.example.coroutinedemo.ui.main

import com.example.coroutinedemo.data.Task

data class TasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val announcement: String = "",
    val courseCount: Int = 0,
    val loadDurationMs: Long? = null,
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val tapCount: Int = 0
) {
    val filteredTasks: List<Task>
        get() = if (searchQuery.isBlank()) {
            tasks
        } else {
            tasks.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.course.contains(searchQuery, ignoreCase = true)
            }
        }
}