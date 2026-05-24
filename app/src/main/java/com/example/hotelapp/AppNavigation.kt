package com.example.hotelapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hotelapp.presentation.auth.LoginScreen
import com.example.hotelapp.presentation.auth.RegisterScreen
import com.example.hotelapp.presentation.bookings.BookingsScreen
import com.example.hotelapp.presentation.hotels.HotelDetailScreen
import com.example.hotelapp.presentation.hotels.HotelsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Hotels : Screen("hotels")
    object HotelDetail : Screen("hotel/{hotelId}") {
        fun createRoute(hotelId: Int) = "hotel/$hotelId"
    }
    object Bookings : Screen("bookings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
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
                },
                onBookingsClick = {
                    navController.navigate(Screen.Bookings.route)
                }
            )
        }

        composable(Screen.HotelDetail.route) { backStackEntry ->
            val hotelId = backStackEntry.arguments?.getString("hotelId")?.toIntOrNull() ?: return@composable
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
    }
}