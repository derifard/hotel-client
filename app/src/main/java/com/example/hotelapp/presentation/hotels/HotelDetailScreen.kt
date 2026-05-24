package com.example.hotelapp.presentation.hotels

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
    hotelId: Int,
    onBack: () -> Unit,
    onBookingsClick: () -> Unit,
    viewModel: HotelDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var checkIn by remember { mutableStateOf("") }
    var checkOut by remember { mutableStateOf("") }
    var showBookingDialog by remember { mutableStateOf(false) }

    LaunchedEffect(hotelId) {
        viewModel.loadHotel(hotelId)
    }

    LaunchedEffect(state.isBookingSuccess) {
        if (state.isBookingSuccess) {
            showBookingDialog = false
            onBookingsClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.hotel?.name ?: "Отель") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.hotel != null -> {
                val hotel = state.hotel!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(text = hotel.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "${hotel.city}, ${hotel.country}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = hotel.rating.toString(), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = hotel.description)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${hotel.pricePerNight}€ за ночь",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    if (state.error != null) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = { showBookingDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Забронировать")
                    }
                }
            }
        }
    }

    if (showBookingDialog) {
        AlertDialog(
            onDismissRequest = { showBookingDialog = false },
            title = { Text("Бронирование") },
            text = {
                Column {
                    OutlinedTextField(
                        value = checkIn,
                        onValueChange = { checkIn = it },
                        label = { Text("Дата заезда (2024-12-01T14:00:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = checkOut,
                        onValueChange = { checkOut = it },
                        label = { Text("Дата выезда (2024-12-05T12:00:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.createBooking(hotel.id, checkIn, checkOut)
                }) {
                    Text("Подтвердить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}