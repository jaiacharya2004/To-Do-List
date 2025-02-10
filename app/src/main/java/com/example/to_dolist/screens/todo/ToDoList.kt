package com.example.to_dolist.screens.todo


import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.example.to_dolist.data.model.Todo

@Composable
fun TodoListScreen(
    navController: NavController,
    todos: State<List<Todo>>,
    onDelete: (Todo) -> Unit,
    onRestore: (Todo) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var recentlyDeletedTodo by remember { mutableStateOf<Todo?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Todo List",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        todos.value.forEach { todo ->
            TodoItem(
                todo = todo,
                navController = navController,
                onDelete = { deletedTodo ->
                    recentlyDeletedTodo = deletedTodo
                    onDelete(deletedTodo)

                    // Show Snackbar for Undo
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Task deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            recentlyDeletedTodo?.let {
                                onRestore(it)
                                recentlyDeletedTodo = null
                            }
                        }
                    }
                }
            )
        }
        SnackbarHost(hostState = snackbarHostState)

    }

}






@Composable
fun TodoItem(
    todo: Todo,
    navController: NavController,
    onDelete: (Todo) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val deleteThreshold = -50f // Threshold for showing delete icon
    var showDelete by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Background (Delete Icon)
        if (showDelete) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = { onDelete(todo) }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }

        // Foreground (Task Card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        offsetX += delta
                        showDelete = offsetX <= deleteThreshold
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (offsetX <= deleteThreshold) {
                            showDelete = true
                        } else {
                            offsetX = 0f
                            showDelete = false
                        }
                    }
                )
                .clickable {
                    navController.navigate("edit_task_screen/${todo.taskName}")
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBB86FC))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todo.category,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Cyan
                    )
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when (todo.status) {
                                "Complete" -> Color.Green
                                "Running" -> Color.Yellow
                                "Pending" -> Color.Red
                                else -> Color.Cyan
                            }
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp),
                    ) {
                        Text(
                            text = todo.status.ifEmpty { "" },
                            modifier = Modifier.padding(start = 15.dp),
                        )
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
}
