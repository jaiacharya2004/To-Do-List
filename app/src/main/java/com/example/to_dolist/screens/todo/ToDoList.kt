package com.example.to_dolist.screens.todo


import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.example.to_dolist.R
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
    val swipeOffsets = remember { mutableStateMapOf<String, Float>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Todo List",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (todos.value.isEmpty()) {
            // Show Image when the task list is empty
            Image(
                painter = painterResource(id = R.drawable.no_task), // Ensure you have an image in res/drawable
                contentDescription = "No Tasks",
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            )
            Text(
                text = "No tasks available. Add a new task!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            todos.value.forEach { todo ->
                TodoItem(
                    todo = todo,
                    navController = navController,
                    onDelete = { deletedTodo ->
                        recentlyDeletedTodo = deletedTodo
                        swipeOffsets.remove(deletedTodo.taskName) // Reset swipe state on delete
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
                    },
                    swipeOffsets = swipeOffsets
                )
            }
        }
        SnackbarHost(hostState = snackbarHostState)
    }
}


@Composable
fun TodoItem(
    todo: Todo,
    navController: NavController,
    onDelete: (Todo) -> Unit,
    swipeOffsets: MutableMap<String, Float>
) {
    var showDelete by remember { mutableStateOf(false) }
    var cardWidth by remember { mutableFloatStateOf(0f) }


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
                .onGloballyPositioned { coordinates ->
                    cardWidth = coordinates.size.width.toFloat()
                }
                .offset { IntOffset(swipeOffsets[todo.taskName]?.roundToInt() ?: 0, 0) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        val maxDragDistance = -cardWidth / 2
                        swipeOffsets[todo.taskName] =
                            (swipeOffsets.getOrDefault(todo.taskName, 0f) + delta).coerceIn(
                                maxDragDistance,
                                0f
                            )
                        showDelete = swipeOffsets[todo.taskName]!! <= maxDragDistance
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (swipeOffsets[todo.taskName]!! <= -cardWidth / 2) {
                            showDelete = true
                        } else {
                            swipeOffsets[todo.taskName] = 0f
                            showDelete = false
                        }
                    }
                )
                .clickable {
                    navController.navigate("edit_task_screen/${todo.id}/${todo.taskName}/${todo.description}/${todo.status}/${todo.category}")
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBB86FC))
        )
        {
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
