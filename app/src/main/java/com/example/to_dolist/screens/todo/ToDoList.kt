package com.example.to_dolist.screens.todo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolist.data.model.Todo
import com.example.to_dolist.screens.home.HomeViewModel
import com.google.firebase.Timestamp
import androidx.compose.runtime.State
import java.util.*

@Composable
fun TodoListScreen(viewModel: HomeViewModel, navController: NavController) {
    val listState = rememberLazyListState()

    val todos: State<List<Todo>> = viewModel.getTodosLiveData().observeAsState(initial = emptyList()) // Correct way
    Log.d("TodoListScreen", "Todos: ${todos.value}")  // Log the list of todos

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Todo List", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))



            if (todos.value.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(todos.value) { todo ->  // Ensure non-null value
                        TodoItem(
                            todo = todo,
                            viewModel = viewModel
                        )
                    }
                }
            } else {
                Text(
                    text = "No tasks available",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }



//            Card (
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.Gray)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Row (
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                          todos.value.forEach { todo ->
//                            Text(
//                                text = todo.taskName
//                            )
//                            Text(
//                                text = todo.description
//                            )
//                            Text(
//                                text = todo.status
//                            )
//                            Text(
//                                text = todo.category
//                            )
//
//                            }
//                }
//            }
//                }







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
fun TodoItem(todo: Todo, viewModel: HomeViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Blue)
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
                    onClick = {
                        // Handle task status change or log the todos
                        viewModel.getTodosLiveData().observeForever {
                            Log.d("TodoList", "Todos: $it")
                        }
                    },

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
