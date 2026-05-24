package com.example.hotelapp.presentation.hotels

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
    hotelId: Int,
    onBack: () -> Unit,
    onBookingsClick: () -> Unit,
    viewModel: HotelDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var checkIn by remember { mutableStateOf("") }
    var checkOut by remember { mutableStateOf("") }
    var showBookingDialog by remember { mutableStateOf(false) }

    val today = Calendar.getInstance()

    fun formatDate(year: Int, month: Int, day: Int): String {
        return "%04d-%02d-%02dT12:00:00".format(year, month + 1, day)
    }

    fun showDatePicker(onDateSelected: (String) -> Unit, minDate: Long = today.timeInMillis) {
        val picker = DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateSelected(formatDate(year, month, day))
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        picker.datePicker.minDate = minDate
        picker.show()
    }

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
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = hotel.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${hotel.city}, ${hotel.country}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = hotel.rating.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Описание",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = hotel.description, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Стоимость",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${hotel.pricePerNight}€ за ночь",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (state.error != null) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = { showBookingDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Забронировать", fontSize = 16.sp)
                    }

                    if (showBookingDialog) {
                        AlertDialog(
                            onDismissRequest = { showBookingDialog = false },
                            title = { Text("Выберите даты") },
                            text = {
                                Column {
                                    OutlinedButton(
                                        onClick = {
                                            showDatePicker(
                                                onDateSelected = { checkIn = it },
                                                minDate = today.timeInMillis
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            if (checkIn.isEmpty()) "Дата заезда"
                                            else checkIn.take(10)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedButton(
                                        onClick = {
                                            val minCheckOut = if (checkIn.isNotEmpty()) {
                                                val cal = Calendar.getInstance()
                                                cal.add(Calendar.DAY_OF_MONTH, 1)
                                                cal.timeInMillis
                                            } else today.timeInMillis
                                            showDatePicker(
                                                onDateSelected = { checkOut = it },
                                                minDate = minCheckOut
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            if (checkOut.isEmpty()) "Дата выезда"
                                            else checkOut.take(10)
                                        )
                                    }

                                    if (checkIn.isNotEmpty() && checkOut.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Заезд: ${checkIn.take(10)}\nВыезд: ${checkOut.take(10)}",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (checkIn.isNotEmpty() && checkOut.isNotEmpty()) {
                                            viewModel.createBooking(hotel.id, checkIn, checkOut)
                                        }
                                    },
                                    enabled = checkIn.isNotEmpty() && checkOut.isNotEmpty()
                                ) {
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
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}