package com.example.hotelapp.domain.model

data class Hotel(
    val id: Int,
    val name: String,
    val city: String,
    val country: String,
    val description: String,
    val pricePerNight: Double,
    val rating: Double,
    val imageUrl: String?
)