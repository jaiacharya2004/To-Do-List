package com.example.to_dolist.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.to_dolist.data.model.Todo
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

class FirestoreHelper {

    private val db = FirebaseFirestore.getInstance()

    // Add Todo to Firestore
    fun addTodo(todo: Todo) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return  // Get current user ID

        // Create a reference to the user's "todos" collection
        val documentRef = db.collection("users").document(userId).collection("todos").document()
        val todoWithId = todo.copy(id = documentRef.id) // Firestore will generate a unique document ID for this todo

        // Save the todo
        documentRef.set(todoWithId)
            .addOnSuccessListener {
                Log.d("FirestoreHelper", "Todo added successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreHelper", "Error adding Todo", exception)
            }
    }


    // Get Todos from Firestore (with real-time updates)
// In FirestoreHelper.kt
    fun getTodos(): LiveData<List<Todo>> {
        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: return MutableLiveData(emptyList())

        val todos = MutableLiveData<List<Todo>>()

        // Fetch todos from the authenticated user's "todos" collection
        db.collection("users").document(userId).collection("todos")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("FirestoreHelper", "Error fetching Todos", exception)
                    return@addSnapshotListener
                }

                val todoList = snapshot?.documents?.mapNotNull { document ->
                    try {
                        document.toObject(Todo::class.java)
                    } catch (e: Exception) {
                        Log.e(
                            "FirestoreHelper",
                            "Error converting document ${document.id}: ${e.message}"
                        )
                        null
                    }
                } ?: emptyList() // Provide an empty list if snapshot is null

                todos.postValue(todoList) // Post the new list
            }

        return todos
    }


    // Delete a specific Todo from Firestore
    fun deleteTodo(todo: Todo) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: return  // Ensure the user is authenticated
        val todoRef: DocumentReference =
            db.collection("users").document(userId).collection("todos")
                .document(todo.id)  // Correct path

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
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: return  // Ensure the user is authenticated

        db.collection("users").document(userId).collection("todos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("users").document(userId).collection("todos")
                        .document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d(
                                "FirestoreHelper",
                                "Todo with ID ${document.id} deleted successfully"
                            )
                        }
                        .addOnFailureListener { exception ->
                            Log.e(
                                "FirestoreHelper",
                                "Error deleting Todo with ID ${document.id}",
                                exception
                            )
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreHelper", "Error getting Todos for deletion", exception)
            }
    }

    fun updateTask(todo: Todo) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: return  // Ensure the user is authenticated

        db.collection("users").document(userId).collection("todos").document(todo.id)
            .set(todo)
            .addOnSuccessListener {
                Log.d("Firestore", "Task updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating task", e)
            }
    }


    fun getTaskByName(taskId: String): LiveData<Todo> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return MutableLiveData()

        val taskLiveData = MutableLiveData<Todo>()
        db.collection("users").document(userId).collection("todos").document(taskId)
            .get()
            .addOnSuccessListener { document ->
                taskLiveData.value = document.toObject(Todo::class.java)
            }

        return taskLiveData
    }
}



