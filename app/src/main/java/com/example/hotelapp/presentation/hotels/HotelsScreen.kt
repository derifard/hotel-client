package com.example.hotelapp.presentation.hotels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hotelapp.domain.model.Hotel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsScreen(
    onHotelClick: (Int) -> Unit,
    viewModel: HotelsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Поиск отелей") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск по городу") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        TextButton(onClick = {
                            viewModel.loadHotels(city = searchQuery)
                        }) { Text("Найти") }
                    }
                }
            )

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
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadHotels() }) { Text("Повторить") }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.hotels) { hotel ->
                            HotelCard(hotel = hotel, onClick = { onHotelClick(hotel.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HotelCard(hotel: Hotel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = hotel.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${hotel.city}, ${hotel.country}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = hotel.rating.toString())
                }
                Text(text = "${hotel.pricePerNight}€/ночь", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}