package com.example.to_dolist.screens.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolist.data.model.Todo
import com.example.to_dolist.navigation.NavigationRoute
import com.example.to_dolist.screens.home.HomeViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewTaskScreen(
    navController: NavController,
    viewmodel: HomeViewModel,
    onTaskCreate: (Todo) -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    val descriptionFocusRequester = remember { FocusRequester() }
    val taskError by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val selectedCategories = remember { mutableStateListOf<String>() }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<String?>(null) }  // Initial value is null (no status selected)


    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create New Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
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
                .padding(start = 16.dp, top = 5.dp),
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
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        descriptionFocusRequester.requestFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = taskError.isNotEmpty()
            )

            // Category Section
            Text(
                text = "Category",
                color = Color.Black,
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
            )
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

            Text("Status", color = Color.Black, fontSize = 25.sp, fontFamily = FontFamily.Serif)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 1.dp, start = 5.dp)
            ) {
                // Create status options: Pending, Running, Complete
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
                                selectedStatus = status  // Set the selected status
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

            // Date & Time Section
            Column(
                modifier = Modifier.padding(top = 10.dp, start = 5.dp)
            ) {
                Text("Date & Time", color = Color.Black, fontSize = 25.sp, fontFamily = FontFamily.Serif)

                Spacer(modifier = Modifier.height(16.dp))
                val context = LocalContext.current // Get context inside composable

                // Date Picker Button
                Button(onClick = {
                    val datePicker = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            selectedDate = dateFormatter.format(calendar.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePicker.show()
                }) {
                    Text(text = if (selectedDate.isEmpty()) "Select Date" else "Date: $selectedDate")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Time Picker Button
                Button(onClick = {
                    val timePicker = TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            selectedTime = timeFormatter.format(calendar.time)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false // 24-hour format false for AM/PM format
                    )
                    timePicker.show()
                }) {
                    Text(text = if (selectedTime.isEmpty()) "Select Time" else "Time: $selectedTime")
                }

                Spacer(modifier = Modifier.height(16.dp))

//                // Display Selected Date and Time
//                if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
//                    Text(
//                        text = "Selected Date: $selectedDate\nSelected Time: $selectedTime",
//                        fontSize = 16.sp,
//                        color = Color.Gray
//                    )
//                }
            }

            // Description Section
            Text("Description", color = Color.Black, fontSize = 25.sp, fontFamily = FontFamily.Serif)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .focusRequester(descriptionFocusRequester)
            )

            // Create Task Button
            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFFA78BFA)),
                onClick = {
                    if (taskName.isBlank() || selectedDate.isEmpty() || selectedTime.isEmpty() || selectedStatus == null) {
                        // Handle validation: Show an error message or disable button
                        return@Button
                    }

                    val categoryText = if (selectedCategories.isNotEmpty()) selectedCategories.joinToString(", ") else "Uncategorized"

                    // Combine selected date and time into a Timestamp
                    val combinedDateTime = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault()).parse("$selectedDate $selectedTime")
                    val timestamp = combinedDateTime?.let { Timestamp(it) } ?: Timestamp.now()

                    val newTask = Todo(
                        taskName = taskName,
                        category = categoryText,
                        description = description,
                        time = timestamp,
                        status = selectedStatus ?: "null" // Default to "Pending" if null
                    )
                    viewmodel.addTodo(newTask)
                    viewmodel.getTodosLiveData()


                    onTaskCreate(newTask)

                    navController.navigate(NavigationRoute.HomeScreen.route) {
                        popUpTo(NavigationRoute.CreateNewScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Create Task")
            }

        }
    }
}

