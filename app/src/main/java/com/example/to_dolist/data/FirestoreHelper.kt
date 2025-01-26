package com.example.to_dolist.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.to_dolist.data.model.Todo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

class FirestoreHelper {

    private val db = FirebaseFirestore.getInstance()

    // Add Todo to Firestore
    fun addTodo(todo: Todo) {
        db.collection("todos")
            .add(todo)
            .addOnSuccessListener {
                Log.d("FirestoreHelper", "Todo added successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreHelper", "Error adding Todo", exception)
            }
    }

    // Get Todos from Firestore (with real-time updates)
    fun getTodos(): LiveData<List<Todo>> {
        val todos = MutableLiveData<List<Todo>>()
        db.collection("todos")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("FirestoreHelper", "Error getting Todos", exception)
                    return@addSnapshotListener
                }
                val todoList = snapshot?.documents?.map { document ->
                    document.toObject(Todo::class.java) ?: Todo()
                } ?: emptyList()
                todos.postValue(todoList)
            }
        return todos
    }

    // Delete a specific Todo from Firestore
    fun deleteTodo(todo: Todo) {
        val todoRef: DocumentReference = db.collection("todos").document(todo.id)
        todoRef.delete()
            .addOnSuccessListener {
                Log.d("FirestoreHelper", "Todo deleted successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreHelper", "Error deleting Todo", exception)
            }
    }

    // Optionally: Delete all Todos (use with caution)
    fun deleteAllTodos() {
        db.collection("todos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("todos").document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d("FirestoreHelper", "Todo with ID ${document.id} deleted successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FirestoreHelper", "Error deleting Todo with ID ${document.id}", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreHelper", "Error getting Todos for deletion", exception)
            }
    }
}
