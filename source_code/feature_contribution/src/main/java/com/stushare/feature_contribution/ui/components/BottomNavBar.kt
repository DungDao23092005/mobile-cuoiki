package com.stushare.feature_contribution.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.stushare.feature_contribution.navigation.Screen
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.Search to Icons.Default.Search,
        Screen.Upload to Icons.Default.AddCircle, // Nút Upload ở giữa
        Screen.Noti to Icons.Default.Notifications,
        Screen.Profile to Icons.Default.Person
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = GreenPrimary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { (screen, icon) ->
            val isUpload = screen == Screen.Upload
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        // Làm nút Upload to hơn và nổi bật
                        modifier = if (isUpload) Modifier.size(48.dp) else Modifier.size(24.dp),
                        tint = if (isUpload) GreenPrimary else if (isSelected) GreenPrimary else Color.Gray
                    )
                },
                selected = isSelected,
                // Ẩn label để giống thiết kế cũ
                label = null, 
                onClick = {
                    navController.navigate(screen.route) {
                        // Logic để tránh chồng stack khi bấm nhiều lần
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // Bỏ nền oval mặc định của Material3
                )
            )
        }
    }
}