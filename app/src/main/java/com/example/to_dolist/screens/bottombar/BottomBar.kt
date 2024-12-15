package com.example.to_dolist.screens.bottombar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.to_dolist.R
import com.example.to_dolist.navigation.NavigationRoute

@Composable
fun BottomBar(navController: NavHostController) {
    val bottomBarColor = Color.Black.copy(alpha = 0.8f)

    Card(
        colors = CardColors(
            containerColor = bottomBarColor,
            contentColor = Color.White,
            disabledContentColor = Color.Gray,
            disabledContainerColor = bottomBarColor
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(end = 18.dp, bottom = 5.dp)
            .fillMaxWidth()
    ) {
        BottomAppBar(
            actions = {
                Row(
//                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = bottomBarColor),
                        shape = CircleShape,
                    ) {
                        IconButton(onClick = {
                            navController.navigate(NavigationRoute.HomeScreen.route)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable._d_house), // Your vector drawable
                                contentDescription = "Home Icon for bottom bar",
                                modifier = Modifier.size(48.dp), // Adjust size as needed
                                tint = Color.Unspecified // Prevent tinting (shows the original color of the icon)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(90.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = bottomBarColor),
                        shape = CircleShape,
                    ) {
                        IconButton(onClick = {
                            navController.navigate(NavigationRoute.PomodoroScreen.route)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.sand_clock), // Your vector drawable
                                contentDescription = "Pomodoro Icon",
                                modifier = Modifier.size(48.dp), // Adjust size as needed
                                tint = Color.Unspecified // Prevent tinting (shows the original color of the icon)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(90.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = bottomBarColor),
                        shape = CircleShape
                    ) {
                        IconButton(onClick = {
                            navController.navigate(NavigationRoute.ProfileScreen.route)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.user__3_), // Your vector drawable
                                contentDescription = "Profile Icon",
                                modifier = Modifier.size(48.dp), // Adjust size as needed
                                tint = Color.Unspecified // Prevent tinting (shows the original color of the icon)
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .height(60.dp)
                .fillMaxSize()
                .padding(top = 1.dp),
            containerColor = bottomBarColor,
            contentColor = Color.White, // This sets the overall content color of the BottomAppBar
        )
    }
}
