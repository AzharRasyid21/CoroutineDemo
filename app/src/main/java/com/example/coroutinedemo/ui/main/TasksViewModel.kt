package com.example.coroutinedemo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutinedemo.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.system.measureTimeMillis

class TasksViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadData(shouldFail: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    loadDurationMs = null
                )
            }
            try {
                var duration = 0L
                coroutineScope {
                    duration = measureTimeMillis {
                        val tasksDeferred = async { repository.getTasks(shouldFail) }
                        val announcementDeferred = async { repository.getAnnouncement() }
                        val courseCountDeferred = async { repository.getCourseCount() }

                        val loadedTasks = tasksDeferred.await()
                        val loadedAnnouncement = announcementDeferred.await()
                        val loadedCourseCount = courseCountDeferred.await()

                        _uiState.update {
                            it.copy(
                                tasks = loadedTasks,
                                announcement = loadedAnnouncement,
                                courseCount = loadedCourseCount
                            )
                        }
                    }
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loadDurationMs = duration
                    )
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load: ${e.message}"
                    )
                }
            }
        }
    }

    fun cancelLoading() {
        loadJob?.cancel()
        _uiState.update {
            it.copy(isLoading = false, errorMessage = "Loading cancelled")
        }
    }

    fun onTap() {
        _uiState.update { it.copy(tapCount = it.tapCount + 1) }
    }

    fun blockMainThread() {
        Thread.sleep(3000L)
    }

    fun sortTasksByTitle() {
        val currentTasks = _uiState.value.tasks
        if (currentTasks.isEmpty()) return

        viewModelScope.launch {
            val sortedList = withContext(Dispatchers.Default) {
                currentTasks.sortedBy { it.title.lowercase() }
            }
            _uiState.update { it.copy(tasks = sortedList) }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleTaskCompleted(taskId: Int) {
        _uiState.update { state ->
            val updatedTasks = state.tasks.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
            state.copy(tasks = updatedTasks)
        }
    }
}