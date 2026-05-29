package com.example.hotelapp.presentation.hotels

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
            { _, year, month, day -> onDateSelected(formatDate(year, month, day)) },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        picker.datePicker.minDate = minDate
        picker.show()
    }

    LaunchedEffect(hotelId) { viewModel.loadHotel(hotelId) }

    LaunchedEffect(state.isBookingSuccess) {
        if (state.isBookingSuccess) {
            showBookingDialog = false
            onBookingsClick()
        }
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        }
        state.hotel != null -> {
            val hotel = state.hotel!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    if (hotel.imageUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(hotel.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = hotel.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            error = painterResource(android.R.drawable.ic_menu_gallery),
                            placeholder = painterResource(android.R.drawable.ic_menu_gallery)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(GreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Hotel,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = GreenPrimary
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent)
                                )
                            )
                    )
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 16.dp, start = 4.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = hotel.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = "${hotel.city}, ${hotel.country}", color = Color.Gray, fontSize = 14.sp)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(GreenLight, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = hotel.rating.toString(), fontWeight = FontWeight.Bold, color = GreenPrimary, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Описание", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = hotel.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "от ${hotel.pricePerNight.toInt()}€", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                            Text(text = "за ночь", fontSize = 12.sp, color = Color.Gray)
                        }
                        Button(
                            onClick = { showBookingDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("Забронировать", fontSize = 15.sp)
                        }
                    }

                    if (state.error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            if (showBookingDialog) {
                AlertDialog(
                    onDismissRequest = { showBookingDialog = false },
                    title = { Text("Выберите даты", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            OutlinedButton(
                                onClick = {
                                    showDatePicker(
                                        onDateSelected = { checkIn = it },
                                        minDate = today.timeInMillis
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                            ) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (checkIn.isEmpty()) "Дата заезда" else checkIn.take(10))
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
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                            ) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (checkOut.isEmpty()) "Дата выезда" else checkOut.take(10))
                            }

                            if (checkIn.isNotEmpty() && checkOut.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = GreenLight),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "Заезд: ${checkIn.take(10)}", fontSize = 13.sp, color = GreenPrimary)
                                        Text(text = "Выезд: ${checkOut.take(10)}", fontSize = 13.sp, color = GreenPrimary)
                                    }
                                }
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
                            enabled = checkIn.isNotEmpty() && checkOut.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Забронировать")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = { showBookingDialog = false },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                        ) {
                            Text("Отмена")
                        }
                    },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}