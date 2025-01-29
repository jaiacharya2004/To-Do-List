package com.example.to_dolist.screens.todo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolist.data.model.Todo
import com.example.to_dolist.screens.home.HomeViewModel
import com.google.firebase.Timestamp
import java.util.*

@Composable
fun TodoListScreen(viewModel: HomeViewModel, navController: NavController) {
    val todos = viewModel.getTodosLiveData().observeAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Todo List", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray)  // Add a background to see the space it occupies
            ) {
                items(todos.value) { todo ->
                    Log.d("TodoList", "Todo Item: ${todo.taskName}")
                    TodoItem(
                        todo = todo,
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
        }

        // Floating Action Button to navigate to CreateNewTaskScreen
        FloatingActionButton(
            onClick = { navController.navigate("create_task_screen") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo")
        }
    }
}


@Composable
fun TodoItem(todo: Todo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Row with Category and Complete Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = todo.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
                Button(
                    onClick = { /* Handle task status update here */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (todo.status) {
                            "Completed" -> Color(0xFF4CAF50)
                            "Running" -> Color(0xFFFF9800)
                            "Pending" -> Color(0xFFFFC107)
                            else -> Color(0xFFE0E0E0)
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(text = todo.status.ifEmpty { "" })
                }
            }


            // Task Name
            Text(
                text = todo.taskName,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                color = Color.Black
            )

            // Description (optional)
            if (todo.description.isNotEmpty()) {
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
        }
    }
}
