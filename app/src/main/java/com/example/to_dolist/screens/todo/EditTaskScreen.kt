package com.example.to_dolist.screens.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.to_dolist.screens.home.HomeViewModel

@Composable
fun EditTaskScreen(taskId: String, viewModel: HomeViewModel, navController: NavController) {
    val task by viewModel.getTaskById(taskId).observeAsState() // Fetch task from Firestore
    var taskName by remember { mutableStateOf(task?.taskName ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    val status by remember { mutableStateOf(task?.status ?: "Pending") }

//    if (task == null) {
//        Text("Loading task...")
//        return
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(value = taskName, onValueChange = { taskName = it }, label = { Text("Task Name") })
        TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        Button(
            onClick = {
                viewModel.updateTask(task!!.copy(taskName = taskName, description = description, status = status
                ))
                navController.popBackStack() // Go back after saving
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}
