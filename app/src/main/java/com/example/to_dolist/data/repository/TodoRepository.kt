package com.example.to_dolist.data.repository

import androidx.lifecycle.LiveData
import com.example.to_dolist.data.FirestoreHelper
import com.example.to_dolist.data.model.Todo

class TodoRepository {

    private val firestoreHelper = FirestoreHelper()

    // Add Todo to Firestore
     fun addTodo(todo: Todo) {
        firestoreHelper.addTodo(todo)
    }

    // Get Todos from Firestore
     fun getTodos(): LiveData<List<Todo>> {
        return firestoreHelper.getTodos()
    }

    // Delete a specific Todo from Firestore
     fun deleteTodo(todo: Todo) {
        firestoreHelper.deleteTodo(todo)
    }

    // Delete all Todos from Firestore
     fun deleteAllTodos() {
        firestoreHelper.deleteAllTodos()
    }
}
