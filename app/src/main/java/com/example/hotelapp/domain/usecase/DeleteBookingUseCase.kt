package com.example.hotelapp.domain.usecase

import com.example.hotelapp.domain.repository.BookingRepository
import javax.inject.Inject

class DeleteBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(bookingId: Int): Boolean {
        return repository.deleteBooking(bookingId)
    }
}