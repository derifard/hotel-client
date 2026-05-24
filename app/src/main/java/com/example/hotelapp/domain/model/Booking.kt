package com.example.hotelapp.domain.model

data class Booking(
    val id: Int,
    val userId: Int,
    val hotelId: Int,
    val hotelName: String = "",
    val hotelCity: String = "",
    val checkIn: String,
    val checkOut: String,
    val totalPrice: Double
)