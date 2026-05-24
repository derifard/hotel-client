package com.example.hotelapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hotelapp.presentation.auth.LoginScreen
import com.example.hotelapp.presentation.auth.RegisterScreen
import com.example.hotelapp.presentation.bookings.BookingsScreen
import com.example.hotelapp.presentation.hotels.HotelDetailScreen
import com.example.hotelapp.presentation.hotels.HotelsScreen
import com.example.hotelapp.presentation.profile.ProfileScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Hotels : Screen("hotels")
    object HotelDetail : Screen("hotel/{hotelId}") {
        fun createRoute(hotelId: Int) = "hotel/$hotelId"
    }
    object Bookings : Screen("bookings")
    object Profile : Screen("profile")
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Отели", Icons.Default.Home, Screen.Hotels.route),
    BottomNavItem("Бронирования", Icons.Default.Bookmark, Screen.Bookings.route),
    BottomNavItem("Профиль", Icons.Default.Person, Screen.Profile.route)
)

val bottomNavRoutes = listOf(Screen.Hotels.route, Screen.Bookings.route, Screen.Profile.route)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Hotels.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Hotels.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Hotels.route) {
                HotelsScreen(
                    onHotelClick = { hotelId ->
                        navController.navigate(Screen.HotelDetail.createRoute(hotelId))
                    }
                )
            }

            composable(Screen.HotelDetail.route) { backStackEntry ->
                val hotelId = backStackEntry.arguments?.getString("hotelId")?.toIntOrNull()
                    ?: return@composable
                HotelDetailScreen(
                    hotelId = hotelId,
                    onBack = { navController.popBackStack() },
                    onBookingsClick = { navController.navigate(Screen.Bookings.route) }
                )
            }

            composable(Screen.Bookings.route) {
                BookingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}