package com.example.to_dolist.screens.home

import com.example.to_dolist.data.model.Todo
import com.example.to_dolist.data.repository.TodoRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel : ViewModel() {

    private val _todos = MutableLiveData<List<Todo>>()
    private val todos: LiveData<List<Todo>> get() = _todos

    private val todoRepository = TodoRepository()


    init {
        viewModelScope.launch {
            // Switch to IO thread for background tasks like fetching data
            withContext(Dispatchers.IO) {
                fetchTodos()
            }
        }    }
    fun getTodosLiveData(): LiveData<List<Todo>> {
        return todos
    }


    private suspend fun fetchTodos() {
        withContext(Dispatchers.Main) {
            todoRepository.getTodos().observeForever { todoList ->
                _todos.postValue(todoList) // Posting new value to LiveData
            }
        }
    }

    // Add Todo to Firestore
    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.addTodo(todo)
            fetchTodos() // Refresh the list after adding a new task
        }
    }



    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }

    // Delete all Todos
    fun deleteAllTodos(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteAllTodos()  // This should call the method that deletes all todos
        }
    }

    fun getTaskById(taskId: String): LiveData<Todo> {
        return todoRepository.getTaskById(taskId)
    }

    // **New Method: Update Task**
    fun updateTask(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTask(todo)
        }
    }




}