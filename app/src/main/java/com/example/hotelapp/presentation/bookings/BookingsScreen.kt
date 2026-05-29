package com.example.hotelapp.presentation.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hotelapp.domain.model.Booking
import com.example.hotelapp.presentation.hotels.GreenLight
import com.example.hotelapp.presentation.hotels.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    onBack: () -> Unit,
    viewModel: BookingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var bookingToDelete by remember { mutableStateOf<Booking?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои бронирования", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadBookings() },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) { Text("Повторить") }
                    }
                }
            }
            state.bookings.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "У вас пока нет бронирований",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.bookings) { booking ->
                        BookingCard(
                            booking = booking,
                            isDeleting = state.deletingId == booking.id,
                            onDeleteClick = { bookingToDelete = booking }
                        )
                    }
                }
            }
        }
    }

    bookingToDelete?.let { booking ->
        AlertDialog(
            onDismissRequest = { bookingToDelete = null },
            title = { Text("Отменить бронь?", fontWeight = FontWeight.Bold) },
            text = { Text("Вы уверены что хотите отменить бронирование в ${booking.hotelName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteBooking(booking.id)
                        bookingToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Да, отменить") }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { bookingToDelete = null },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                ) { Text("Нет, оставить") }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun BookingCard(
    booking: Booking,
    isDeleting: Boolean,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(GreenLight, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Hotel,
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (booking.hotelName.isNotEmpty()) booking.hotelName else "Отель #${booking.hotelId}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (booking.hotelCity.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = booking.hotelCity,
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Заезд", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = booking.checkIn.take(10), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Выезд", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = booking.checkOut.take(10), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Итого", color = Color.Gray, fontSize = 13.sp)
                Text(
                    text = "${booking.totalPrice}€",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isDeleting) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = GreenPrimary)
                }
            } else {
                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Отменить бронирование")
                }
            }
        }
    }
}