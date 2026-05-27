package com.example.hotelapp.presentation.bookings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hotelapp.domain.model.Booking

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
                title = { Text("Мои бронирования") },
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
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadBookings() }) { Text("Повторить") }
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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "У вас пока нет бронирований",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
            title = { Text("Отмена бронирования") },
            text = { Text("Вы уверены что хотите отменить бронирование в ${booking.hotelName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteBooking(booking.id)
                        bookingToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Отменить бронь") }
            },
            dismissButton = {
                TextButton(onClick = { bookingToDelete = null }) { Text("Назад") }
            }
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
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Hotel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (booking.hotelName.isNotEmpty()) booking.hotelName
                    else "Отель #${booking.hotelId}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (booking.hotelCity.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = booking.hotelCity,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Заезд", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    Text(text = booking.checkIn.take(10), fontWeight = FontWeight.Medium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Выезд", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    Text(text = booking.checkOut.take(10), fontWeight = FontWeight.Medium)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Итого:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "${booking.totalPrice}€",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isDeleting) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            } else {
                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.fillMaxWidth(),
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