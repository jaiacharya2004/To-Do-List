package com.example.to_dolist.screens.todo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodoListScreen(viewModel: HomeViewModel, navController: NavController) {
    val listState = rememberLazyListState()

    val todos: State<List<Todo>> = viewModel.getTodosLiveData().observeAsState(initial = emptyList())
    Log.d("TodoListScreen", "Todos: ${todos.value}")  // Log the list of todos



    Log.d("TodoListScreen", "Todos observed: ${todos.value.size} items") // Log the size
    todos.value.forEach { todo ->
        Log.d("TodoListScreen", "Todo: $todo") // Log each Todo object
    }

     Column(
            modifier = Modifier
                .fillMaxSize()


     ) {
            Text(text = "Todo List", style = MaterialTheme.typography.headlineSmall)

         FloatingActionButton(
             onClick = { navController.navigate("create_task_screen") },
             modifier = Modifier
                 .padding(16.dp)
         ) {
             Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo")
         }
            Spacer(modifier = Modifier.height(16.dp))

         todos.value.forEach { todo ->
             Card(
                 modifier = Modifier
                     .fillMaxWidth()
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
                     // Top Row with Category and Complete Button
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
                         Button(
                             onClick = {
                                 // Handle task status change or log the todos
                                 viewModel.getTodosLiveData().observeForever {
                                     Log.d("TodoList", "Todos: $it")
                                 }
                             },

                             colors = ButtonDefaults.buttonColors(
                                 containerColor = when (todo.status) {
                                     "Complete" -> Color.Green
                                     "Running" -> Color.Yellow
                                     "Pending" -> Color.Red
                                     else -> Color.Cyan
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
         Spacer(modifier = Modifier.height(100.dp))

     }


}


