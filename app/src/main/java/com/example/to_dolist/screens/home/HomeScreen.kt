package com.example.to_dolist.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.to_dolist.R
import com.example.to_dolist.navigation.NavigationRoute
import com.example.to_dolist.screens.bottombar.BottomBar
import com.example.to_dolist.screens.profile.ProfileImage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController) {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.displayName ?: "User Name"
    val profileImageUrl = user?.photoUrl?.toString() // Fetch profile image URL from Firebase


    Column (
        modifier = Modifier
            .padding(top = 40.dp, start = 1.dp, end = 1.dp)
            .verticalScroll(rememberScrollState()) // Enable vertical scrolling
            .background(Color.White)
            .fillMaxSize()

        ) {

        Box(
            modifier = Modifier
                .size(60.dp)
                .padding(start = 16.dp, top = 16.dp)
                .clickable {
                    // Navigate to the ProfileScreen when clicked
                    navController.navigate(NavigationRoute.ProfileScreen.route)
                }
        )
        {


        }

        Text(
            "      Hello ! \n      Welcome $userName",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )



        Spacer(modifier = Modifier.height(680.dp))


  }
    Column (
        modifier = Modifier.padding(top = 800.dp)
    ){
        BottomBar(navController)
    }

}
