package com.example.to_dolist.screens.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.to_dolist.R
import com.example.to_dolist.navigation.NavigationRoute

@Composable
fun BottomBar(navController: NavHostController) {
    val bottomBarColor = Color(0xFF6200EE) // Purple Background
    val selectedIconColor = Color.White
    val unselectedIconColor = Color(0xFFBB86FC) // Light Purple

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    // List of tabs
    val tabs = listOf(
        BottomBarTab("Home", R.drawable._d_house, NavigationRoute.HomeScreen.route),
        BottomBarTab("Pomodoro", R.drawable.sand_clock, NavigationRoute.PomodoroScreen.route),
        BottomBarTab("Profile", R.drawable.user__3_, NavigationRoute.ProfileScreen.route),
        BottomBarTab("State", R.drawable.exploratory_analysis_12489862, NavigationRoute.StateScreen.route)
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = bottomBarColor),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                BottomBarItem(
                    tab = tab,
                    isSelected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                    onClick = {
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    selectedColor = selectedIconColor,
                    unselectedColor = unselectedIconColor
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    tab: BottomBarTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    unselectedColor: Color
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                if (isSelected) selectedColor.copy(alpha = 0.2f) else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = tab.icon),
                contentDescription = tab.label,
                tint = if (isSelected) selectedColor else unselectedColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// Data class for BottomBarTab
data class BottomBarTab(
    val label: String,
    val icon: Int,
    val route: String
)
