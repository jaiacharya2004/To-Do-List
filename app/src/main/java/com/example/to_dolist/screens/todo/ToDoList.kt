package com.example.to_dolist.screens.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import com.example.to_dolist.data.model.Todo
import com.example.to_dolist.screens.home.HomeViewModel

@Composable
fun TodoListScreen(viewModel: HomeViewModel) {
    val todos = viewModel.getTodosLiveData().observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Todo List", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Wrap LazyColumn in a fixed-height container (e.g., Box)
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()  // Let LazyColumn take up all available space
            ) {
                items(todos.value) { todo ->
                    TodoItem(todo = todo, onDelete = {
                        // Handle delete action (you can add your logic here)
                        viewModel.deleteAllTodos(todo)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Todo Button
        Button(
            onClick = {
                val newTodo = Todo(
                    title = "New Todo",
                    description = "Description of the new todo",
                    time = "2:00 PM",
                    alarm = true
                )
                viewModel.addTodo(newTodo)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Todo")
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = todo.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = todo.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Time: ${todo.time}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Delete Button
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Delete", color = Color.White)
            }
        }
    }
}

