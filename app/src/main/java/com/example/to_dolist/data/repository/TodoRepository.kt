package com.example.to_dolist.data.repository

import androidx.lifecycle.LiveData
import com.example.to_dolist.data.FirestoreHelper
import com.example.to_dolist.data.model.Todo

class TodoRepository {
    private val firestoreHelper = FirestoreHelper()

    fun addTodo(todo: Todo) {
        firestoreHelper.addTodo(todo)
    }

    fun getTodos(): LiveData<List<Todo>> {
        return firestoreHelper.getTodos()
    }

    fun deleteTodo(todo: Todo) {
        firestoreHelper.deleteTodo(todo)
    }

    fun deleteAllTodos() {
        firestoreHelper.deleteAllTodos()
    }

    // **New Method: Get Task by ID (or name)**
    fun getTaskById(taskId: String): LiveData<Todo> {
        return firestoreHelper.getTaskByName(taskId)
    }


    // **New Method: Update Task**
    fun updateTask(todo: Todo) {
        firestoreHelper.updateTask(todo)  // Calls FirestoreHelper function
    }

    fun restoreTask(todo: Todo) {
        firestoreHelper.addTodo(todo) // Re-add task to Firestore
    }


}
