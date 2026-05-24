package com.example.hotelapp.data.repository

import com.example.hotelapp.data.remote.api.HotelApi
import com.example.hotelapp.data.remote.dto.CreateBookingRequestDto
import com.example.hotelapp.domain.model.Booking
import com.example.hotelapp.domain.repository.BookingRepository
import com.example.hotelapp.domain.repository.AuthRepository
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val api: HotelApi,
    private val authRepository: AuthRepository
) : BookingRepository {

    override suspend fun createBooking(hotelId: Int, checkIn: String, checkOut: String): Booking {
        val token = authRepository.getToken() ?: throw Exception("Не авторизован")
        return api.createBooking(
            token = "Bearer $token",
            request = CreateBookingRequestDto(hotelId, checkIn, checkOut)
        ).toDomain()
    }

    override suspend fun getMyBookings(): List<Booking> {
        val token = authRepository.getToken() ?: throw Exception("Не авторизован")
        return api.getMyBookings("Bearer $token").map { it.toDomain() }
    }
}