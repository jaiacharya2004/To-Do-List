package com.example.to_dolist.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.to_dolist.R
import com.example.to_dolist.navigation.NavigationRoute
import com.example.to_dolist.screens.bottombar.BottomBar
import com.example.to_dolist.screens.calendar.ScrollableCalendar
import com.example.to_dolist.screens.profile.ProfileViewModel
import com.example.to_dolist.screens.todo.TodoListScreen
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

@Composable
fun HomeScreen(navController: NavHostController, viewModel: ProfileViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.displayName ?: "User Name"
    val profileImageUri by viewModel.profileImageUri.collectAsState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Use Box to ensure consistent background color across the entire screen
    Box(
        modifier = Modifier
            .fillMaxSize() // Ensures the Box takes up the entire screen
            .background(Color(0xFFFFFAF0)) // Background color
    ) {
        // Column with padding inside the Box
        Column(
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Row: Profile Image and Greeting
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                Image(
                    painter = if (profileImageUri != null) {
                        rememberAsyncImagePainter(model = profileImageUri)
                    } else {
                        painterResource(id = R.drawable.profilephoto)
                    },
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable {  }
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Greeting Text
                Text(
                    text = "Hello!\n$userName",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp
                )


            }

            // Scrollable Calendar
            ScrollableCalendar(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    // Load tasks or perform actions for the selected date
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Example Section: Selected Date Tasks (Placeholder)
            Text(
                text = "Tasks for ${selectedDate.dayOfMonth} ${selectedDate.month}",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))

            Divider(color = Color.Gray, thickness = 1.dp)

            TodoListScreen(viewModel = HomeViewModel(),navController)



        }

        Column (
            modifier = Modifier.padding(top = 805.dp)
        ){BottomBar(navController)  }

    }
}
