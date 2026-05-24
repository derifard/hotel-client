package com.example.hotelapp.domain.repository

import com.example.hotelapp.domain.model.Hotel

interface HotelRepository {
    suspend fun getHotels(city: String? = null, maxPrice: Double? = null): List<Hotel>
    suspend fun getHotelById(id: Int): Hotel?
}