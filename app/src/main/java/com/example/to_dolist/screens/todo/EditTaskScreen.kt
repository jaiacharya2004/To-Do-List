package com.example.to_dolist.screens.todo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolist.data.model.Todo
import com.example.to_dolist.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: String,
    taskName: String,
    description: String,
    status: String,
    category: String,
    navController: NavController,
    viewModel: HomeViewModel
) {
    // Fetch task by ID
    val task by viewModel.getTaskById(taskId).observeAsState()

    var taskName by remember { mutableStateOf(task?.taskName ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var selectedStatus by remember { mutableStateOf(task?.status ?: "Pending") }
    val selectedCategories = remember { mutableStateListOf(*task?.category?.split(", ")?.toTypedArray() ?: arrayOf()) }

    val id by remember { mutableStateOf(task?.id ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current






    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(start = 16.dp, top = 25.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Task Name Section
            Text("Task Name", color = Color.Black, fontSize = 25.sp, fontFamily = FontFamily.Serif)
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task Name") },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Section
            Text("Category", color = Color.Black, fontSize = 25.sp, fontFamily = FontFamily.Serif)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 1.dp)
            ) {
                items(listOf("Work", "Personal", "Project", "Shopping", "College", "Study", "Health")) { category ->
                    val isSelected = category in selectedCategories
                    Box(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = if (isSelected) Color.Cyan else Color(0xFFD3D3D3),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                color = if (isSelected) Color(0xFF6200EE) else Color(0xFFFFFFF0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable {
                                if (isSelected) {
                                    selectedCategories.remove(category)
                                } else {
                                    selectedCategories.add(category)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Section
            Text("Status", color = Color.Black, fontSize = 25.sp, fontFamily = FontFamily.Serif)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 1.dp, start = 5.dp)
            ) {
                val statuses = listOf("Pending", "Running", "Complete")

                statuses.forEach { status ->
                    val isSelected = status == selectedStatus
                    Box(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = if (isSelected) Color.Cyan else Color(0xFFD3D3D3),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                color = if (isSelected) Color(0xFF6200EE) else Color(0xFFFFFFF0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable {
                                selectedStatus = status
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = status,
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            Text("Description", color = Color.Black, fontSize = 25.sp, fontFamily = FontFamily.Serif)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFFA78BFA)),
                onClick = {
                    if (taskName.isBlank() || selectedStatus.isNullOrBlank()) {
                        // Handle validation: Show an error message or disable button
                        return@Button
                    }

                    val categoryText = if (selectedCategories.isNotEmpty()) selectedCategories.joinToString(", ") else "Uncategorized"

                    // Ensure that the taskId is correctly retrieved from the task data
                    if (taskId.isBlank()) {
                        Log.e("EditTaskScreen", "Error: taskId is blank.")
                        return@Button
                    }

                    // Create updated task with the correct taskId
                    val updatedTask = Todo(
                        id = taskId, // Use the correct taskId here
                        taskName = taskName,
                        category = categoryText,
                        description = description,
                        status = selectedStatus
                    )

                    // Update task in Firestore using the correct taskId
                    viewModel.updateTask(updatedTask)

                    // Navigate back
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Save Task")
            }


        }
    }
}
