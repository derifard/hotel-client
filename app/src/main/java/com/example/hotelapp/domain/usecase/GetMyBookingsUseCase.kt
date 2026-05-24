package com.example.hotelapp.domain.usecase

import com.example.hotelapp.domain.model.Booking
import com.example.hotelapp.domain.repository.BookingRepository
import javax.inject.Inject

class GetMyBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(): List<Booking> {
        return repository.getMyBookings()
    }
}