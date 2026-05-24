package com.example.hotelapp.domain.repository

import com.example.hotelapp.domain.model.Booking

interface BookingRepository {
    suspend fun createBooking(hotelId: Int, checkIn: String, checkOut: String): Booking
    suspend fun getMyBookings(): List<Booking>
}