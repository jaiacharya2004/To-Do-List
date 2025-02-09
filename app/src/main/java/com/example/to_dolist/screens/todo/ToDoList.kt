package com.example.to_dolist.screens.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolist.data.model.Todo
import androidx.compose.runtime.State
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun TodoListScreen( navController: NavController,todos:State<List<Todo>>) {

     Column(
            modifier = Modifier
                .fillMaxSize()

     ) {

            Text(text = "Todo List", style = MaterialTheme.typography.headlineSmall)


            Spacer(modifier = Modifier.height(16.dp))

         todos.value.forEach { todo ->
           Todo(todo,navController)
         }

         Spacer(modifier = Modifier.height(100.dp))

     }


}
@Composable
fun Todo(todo: Todo,navController: NavController,){

    var offsetX by remember { mutableStateOf(0f) }
    var showDelete by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable(
                state = rememberDraggableState(
                    onDelta = { delta ->
                        offsetX +=   delta
                        showDelete = offsetX < 0 // Show delete icon when swiped
                    },

                ),
                orientation = Orientation.Horizontal,

            )
            .clickable {
                navController.navigate("edit_task_screen/${todo.taskName}")

            }
            .padding(8.dp),
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
                    colors = CardDefaults.cardColors(containerColor = when (todo.status) {
                        "Complete" -> Color.Green
                        "Running" -> Color.Yellow
                        "Pending" -> Color.Red
                        else -> Color.Cyan
                    }),
                    modifier = Modifier
                        .width(100.dp).height(30.dp),


                ) {
                    Text(
                        text = todo.status.ifEmpty { "" },modifier = Modifier.padding(start = 15.dp),
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


